package com.github.eitraz.swedbank.authentication;

import com.github.eitraz.swedbank.SwedbankApi;
import com.github.eitraz.swedbank.SwedbankClient;
import com.github.eitraz.swedbank.bank.BankType;
import com.github.eitraz.swedbank.exception.SwedbankAuthenticationException;
import com.github.eitraz.swedbank.exception.SwedbankClientException;
import com.github.eitraz.swedbank.model.Link;
import com.github.eitraz.swedbank.model.identification.bankid.AuthenticationRequest;
import com.github.eitraz.swedbank.model.identification.bankid.AuthenticationResponse;
import com.github.eitraz.swedbank.model.identification.bankid.IdentificationStatus;
import com.github.eitraz.swedbank.model.identification.bankid.VerifyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MobileBankId {
    private static final Logger logger = LoggerFactory.getLogger(MobileBankId.class);

    private ExecutorService executorService = new ThreadPoolExecutor(
            1, 1, 0, TimeUnit.MILLISECONDS, new SynchronousQueue<>());

    private final AtomicBoolean verified = new AtomicBoolean(false);
    private final SwedbankClient swedbankClient;

    public MobileBankId(BankType bankType) {
        swedbankClient = new SwedbankClient(bankType);
    }

    public Future<SwedbankApi> authenticate(String userId) throws SwedbankAuthenticationException, SwedbankClientException {
        AuthenticationResponse response = swedbankClient.post(
                "identification/bankid/mobile",
                AuthenticationRequest.create(userId),
                AuthenticationResponse.class);

        // Unable to initiate authentication
        if (!response.getStatus().equals(IdentificationStatus.USER_SIGN)) {
            throw new SwedbankAuthenticationException("Unable to use Mobile BankID. It may have to be activated at swedbank.se.");
        }

        final Link verifyLink = response.getLinks().getNext();
        return executorService.submit(() -> {
            while (true) {
                Thread.sleep(5000);

                if (verify(verifyLink)) {
                    return new SwedbankApi(swedbankClient);
                }
            }
        });
    }

    private boolean verify(Link verifyLink) throws SwedbankAuthenticationException, SwedbankClientException {
        if (verified.get())
            return true;

        VerifyResponse response = swedbankClient.follow(verifyLink, VerifyResponse.class);
        IdentificationStatus status = response.getStatus();

        // Verified on "COMPLETE"
        verified.set(status.equals(IdentificationStatus.COMPLETE));

        // Unknown status
        if (status.equals(IdentificationStatus.UNKNOWN)) {
            throw new SwedbankAuthenticationException("Mobile BankID cannot be verified.");
        }
        // Client not started
        else if (status.equals(IdentificationStatus.CLIENT_NOT_STARTED)) {
            logger.info("Open Mobile Bank ID application on your mobile device.");
        }

        return verified.get();
    }
}

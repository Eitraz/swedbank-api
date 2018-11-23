package com.github.eitraz.swedbank;

import com.github.eitraz.swedbank.exception.SwedbankApiException;
import com.github.eitraz.swedbank.exception.SwedbankClientException;
import com.github.eitraz.swedbank.http.HttpMethod;
import com.github.eitraz.swedbank.model.Link;
import com.github.eitraz.swedbank.model.Links;
import com.github.eitraz.swedbank.model.engagement.Overview;
import com.github.eitraz.swedbank.model.engagement.TransactionAccount;
import com.github.eitraz.swedbank.model.engagement.transactions.Transaction;
import com.github.eitraz.swedbank.model.engagement.transactions.Transactions;
import com.github.eitraz.swedbank.model.profile.Profile;
import com.github.eitraz.swedbank.model.profile.SetProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@SuppressWarnings({"WeakerAccess", "unused"})
public class SwedbankApi implements Closeable {
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(SwedbankApi.class);

    private SwedbankClient swedbankClient;
    private Profile selectedProfile;

    public SwedbankApi(SwedbankClient swedbankClient) {
        this.swedbankClient = swedbankClient;
    }

    private SwedbankClient getSwedbankClient() throws SwedbankClientException, SwedbankApiException {
        if (swedbankClient == null) {
            throw new RuntimeException("Swedbank client have been logged out");
        }

        if (selectedProfile == null) {
            selectedProfile = selectProfile();
        }

        return swedbankClient;
    }

    private Profile selectProfile() throws SwedbankClientException, SwedbankApiException {
        Profile profile = swedbankClient.get("profile/", Profile.class);

        if (profile.getBanks().isEmpty()) {
            throw new SwedbankApiException("The profile does not contain any bank accounts.");
        }

        String profileId = profile.getBanks().get(0).getPrivateProfile().getId();
        swedbankClient.post("profile/" + profileId, null, SetProfileResponse.class);
        return profile;
    }

    public void logout() throws SwedbankClientException {
        swedbankClient.put("identification/logout", null, String.class);
        swedbankClient = null;
    }

    @Override
    public void close() {
        try {
            logout();
        } catch (SwedbankClientException e) {
            throw new RuntimeException(e);
        }
    }

    public Overview getOverview() throws SwedbankClientException, SwedbankApiException {
        return getSwedbankClient().get("engagement/overview", Overview.class);
    }

    public Transactions getTransactions(TransactionAccount account) throws SwedbankClientException, SwedbankApiException {
        return getTransactions(account, 100, 1);
    }

    public Transactions getTransactions(TransactionAccount account, int transactionsPerPage, int page) throws SwedbankClientException, SwedbankApiException {
        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put("transactionsPerPage", String.valueOf(transactionsPerPage));
        queryParameters.put("page", String.valueOf(page));

        return getSwedbankClient().get(
                "engagement/transactions/" + account.getId(),
                queryParameters,
                Transactions.class);
    }

    private Optional<Link> transactionNextLink(Transactions transactions) {
        // More transactions is available
        if (transactions.isMoreTransactionsAvailable()) {
            return ofNullable(transactions.getLinks())
                    .map(Links::getNext);
        }
        // No more transactions
        else {
            return Optional.empty();
        }
    }

    public Iterable<Transaction> getTransactionsIterable(TransactionAccount account) throws SwedbankClientException, SwedbankApiException {
        return getTransactionsIterable(account, 100);
    }

    public Iterable<Transaction> getTransactionsIterable(TransactionAccount account, int transactionsPerPage) throws SwedbankClientException, SwedbankApiException {
        Optional<Link> firstLink = Optional.of(new Link(
                HttpMethod.GET,
                String.format("engagement/transactions/%s?transactionsPerPage=%d&page=1", account.getId(), transactionsPerPage)
        ));

        return new PageIterable<>(
                getSwedbankClient(),
                transactions -> transactions.map(this::transactionNextLink).orElse(firstLink),
                Transactions.class,
                transactions -> transactions.getTransactions().iterator()
        );
    }

}

package com.github.eitraz.swedbank;

import com.github.eitraz.swedbank.exception.SwedbankApiException;
import com.github.eitraz.swedbank.exception.SwedbankClientException;
import com.github.eitraz.swedbank.http.HttpMethod;
import com.github.eitraz.swedbank.model.Link;
import com.github.eitraz.swedbank.model.Links;
import com.github.eitraz.swedbank.model.engagement.Overview;
import com.github.eitraz.swedbank.model.engagement.TransactionAccount;
import com.github.eitraz.swedbank.model.engagement.account.AccountDetails;
import com.github.eitraz.swedbank.model.engagement.transactions.Transaction;
import com.github.eitraz.swedbank.model.engagement.transactions.Transactions;
import com.github.eitraz.swedbank.model.profile.*;
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
    private boolean profileSelected;

    public SwedbankApi(SwedbankClient swedbankClient) {
        this.swedbankClient = swedbankClient;
    }

    private SwedbankClient getSwedbankClient() throws SwedbankClientException, SwedbankApiException {
        if (swedbankClient == null) {
            throw new RuntimeException("Swedbank client have been logged out");
        }

        if (!profileSelected) {
            Profile profile = getProfile();

            if (profile.getBanks().size() == 1) {
                Bank bank = profile.getBanks().get(0);

                // Only private profile available
                if (bank.getPrivateProfile() != null && bank.getCorporateProfiles().isEmpty()) {
                    selectProfile(bank.getPrivateProfile());
                }
                // Only one corporate profile available
                else if (bank.getPrivateProfile() == null && bank.getCorporateProfiles().size() == 1) {
                    selectProfile(bank.getCorporateProfiles().get(0));
                }
                // Unable to auto select profile
                else {
                    throw new SwedbankApiException("Multiple profiles are available, profile can't be auto selected. Use setProfile() to select a profile.");
                }
            }
            // Multiple banks
            else {
                throw new SwedbankApiException("There are multiple or no banks available, profile can't be auto selected. Use setProfile() to select a profile.");
            }
        }

        return swedbankClient;
    }

    public Profile getProfile() throws SwedbankClientException {
        return swedbankClient.get("profile/", Profile.class);
    }

    public void selectProfile(PrivateProfile privateProfile) throws SwedbankClientException {
        swedbankClient.follow(privateProfile.getLinks().getNext(), SetProfileResponse.class);
        this.profileSelected = true;
    }

    public void selectProfile(CorporateProfile corporateProfile) throws SwedbankClientException {
        swedbankClient.follow(corporateProfile.getLinks().getNext(), SetProfileResponse.class);
        this.profileSelected = true;
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

    public AccountDetails getAccountDetails(TransactionAccount transactionAccount) throws SwedbankClientException, SwedbankApiException {
        Link detailsLink = transactionAccount.getDetails()
                                             .getLinks()
                                             .getNext();

        return getSwedbankClient().follow(detailsLink, AccountDetails.class);
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

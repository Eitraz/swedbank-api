package com.github.eitraz.swedbank;

import com.github.eitraz.swedbank.authentication.MobileBankId;
import com.github.eitraz.swedbank.bank.BankType;
import com.github.eitraz.swedbank.model.engagement.Overview;
import com.github.eitraz.swedbank.model.engagement.TransactionAccount;
import com.github.eitraz.swedbank.model.engagement.account.AccountDetails;
import com.github.eitraz.swedbank.model.engagement.transactions.Transaction;
import com.github.eitraz.swedbank.model.engagement.transactions.Transactions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

class SwedbankApiTest {
    private static SwedbankApi api;

    @BeforeAll
    static void setupClientAndLogin() throws Exception {
        String userId = requireNonNull(System.getProperty("userId"), "System property 'userId' is required (using for example -DuserId=[USER_ID])");

        api = new MobileBankId(BankType.SWEDBANK)
                .authenticate(userId)
                .get(2, TimeUnit.MINUTES);
    }

    @AfterAll
    static void logout() throws Exception {
        if (api != null) {
            api.logout();
        }
    }

    @Test
    void getOverview() throws Exception {
        Overview overview = api.getOverview();

        assertThat(overview.getTransactionAccounts()).isNotNull();
    }

    @Test
    void getAccountDetails() throws Exception {
        Overview overview = api.getOverview();

        TransactionAccount transactionAccount = overview.getTransactionAccounts().get(0);

        AccountDetails details = api.getAccountDetails(transactionAccount);
        assertThat(details.getName().getCurrent()).isEqualTo(transactionAccount.getName());
    }

    @Test
    void getTransactions() throws Exception {
        TransactionAccount transactionAccount = api.getOverview()
                                                   .getTransactionAccounts()
                                                   .get(0);

        Transactions transactions = api.getTransactions(transactionAccount);
        assertThat(transactions.getAccount()).isNotNull();
        assertThat(transactions.getTransactions()).isNotNull();
    }

    @Test
    void getTransactionsIterable() throws Exception {
        TransactionAccount transactionAccount = api.getOverview()
                                                   .getTransactionAccounts()
                                                   .get(0);

        Set<String> transactionIds = new HashSet<>();
        Iterable<Transaction> transactions = api.getTransactionsIterable(transactionAccount, 25);
        int counter = 0;
        for (Transaction transaction : transactions) {
            // Transaction ID may sometimes be null
            if (transaction.getId() != null) {
                assertThat(transactionIds).doesNotContain(transaction.getId());
                transactionIds.add(transaction.getId());
            }

            // Fetch 50 first entries to force page loading
            if (++counter >= 50)
                break;
        }

        // Make sure not all transaction IDs was null
        assertThat(transactionIds).isNotEmpty();
    }
}
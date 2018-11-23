# Swedbank

Unofficial Java API client for Swedbank.

## Authentication
- Mobile BankID

## Features
- Account overview
- List account transactions

## Code examples

Get all accounts balance.
```java
SwedbankApi swedbankApi = null;

try {
    // Login using Mobile BankID
    swedbankApi = new MobileBankId(BankType.SWEDBANK)
            .authenticate(socialSecurityNumber)
            .get(2, TimeUnit.MINUTES);

    // Account overview
    Overview overview = swedbankApi.getOverview();

    // Accounts balance
    for (TransactionAccount account : overview.getTransactionAccounts()) {
        System.out.println(account.getName() + ": " + account.getBalance() + " " + account.getCurrency());
    }
} finally {
    // Manual logout
    if (swedbankApi != null) {
        swedbankApi.logout();
    }
}
```

Get all transactions for all transaction accounts.
```java
// Login using Mobile BankID, auto logout when done
try (SwedbankApi swedbankApi = new MobileBankId(BankType.SWEDBANK)
        .authenticate(socialSecurityNumber)
        .get(2, TimeUnit.MINUTES)) {

    // Account overview
    Overview overview = swedbankApi.getOverview();

    // Iterate all transaction accounts
    for (TransactionAccount account : overview.getTransactionAccounts()) {
        System.out.println("Transactions for " + account.getName());

        // Iterate all transactions
        for (Transaction transaction : swedbankApi.getTransactionsIterable(account)) {
            System.out.println("\t" + transaction.getDate() + ", " + transaction.getAmount() + ", " + transaction.getDescription());
        }
    }
}
```

## Thanks
Heavily based on the work made by https://github.com/walle89/SwedbankJson.

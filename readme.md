# Swedbank

Unofficial Java API client for Swedbank.

## Authentication
- Mobile BankID

## Features
- Account overview
- Transaction account details
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

## Maven
Available at [Maven Repository: Central](https://mvnrepository.com/artifact/com.github.eitraz/swedbank-api/1.1)

```xml
<dependency>
    <groupId>com.github.eitraz</groupId>
    <artifactId>swedbank-api</artifactId>
    <version>1.1</version>
</dependency>
```

## Change log

### 1.0
First release.

### 1.1
Account details.

### 1.2
Corporate details.

### 1.3
Updated Swedbank API version.

## Thanks
Heavily based on the work made by https://github.com/walle89/SwedbankJson.

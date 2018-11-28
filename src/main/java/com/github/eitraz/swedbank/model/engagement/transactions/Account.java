package com.github.eitraz.swedbank.model.engagement.transactions;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.eitraz.swedbank.json.BalanceDeserializer;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class Account {
    @JsonDeserialize(using = BalanceDeserializer.class)
    private Double availableAmount;

    @JsonDeserialize(using = BalanceDeserializer.class)
    private Double reservedAmount;

    @JsonAlias({"quickBalanceSubscription", "quickbalanceSubscription"})
    private QuickbalanceSubscription quickbalanceSubscription;

    private boolean currencyAccount;

    @JsonDeserialize(using = BalanceDeserializer.class)
    private Double creditGranted;

    private boolean internalAccount;
    private String name;
    private String id;
    private String currency;

    @JsonDeserialize(using = BalanceDeserializer.class)
    private Double balance;

    private String clearingNumber;
    private String accountNumber;
    private String fullyFormattedNumber;
    private boolean availableForFavouriteAccount;
    private boolean availableForPriorityAccount;
    private String originalName;
    private ExpenseControl expenseControl;
}

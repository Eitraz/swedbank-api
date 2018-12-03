package com.github.eitraz.swedbank.model.engagement.transactions;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.eitraz.swedbank.json.BalanceDeserializer;
import com.github.eitraz.swedbank.model.engagement.account.Amount;
import lombok.Getter;
import lombok.Setter;

/**
 * Shared class for both private and corporate profile - fields may be null
 */
@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class Transaction {
    private String id;
    private String date;
    private String description;
    private String currency;
    private TransactionDetails details;

    @JsonDeserialize(using = BalanceDeserializer.class)
    private Double amount;

    private String expenseControlIncluded;
    private Amount accountingBalance;
    private Labelings labelings;
    private Categorizations categorizations;
    private String bookedDate;
    private Long categoryId;
}

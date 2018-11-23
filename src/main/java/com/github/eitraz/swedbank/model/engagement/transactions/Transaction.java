package com.github.eitraz.swedbank.model.engagement.transactions;

import com.github.eitraz.swedbank.json.BalanceDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class Transaction {
    private String id;
    private String date;
    private String description;
    private String currency;

    @JsonDeserialize(using = BalanceDeserializer.class)
    private Double amount;

    private String expenseControlIncluded;
    private Labelings labelings;
    private Categorizations categorizations;
}

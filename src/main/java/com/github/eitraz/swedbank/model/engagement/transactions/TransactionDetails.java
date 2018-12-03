package com.github.eitraz.swedbank.model.engagement.transactions;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class TransactionDetails {
    private String message;
    private String id;
    private String reference;
    private String transactionType;
    private String bookedDate;
    private String bankReference;
    private String valueDate;
    private String transactionDate;
}

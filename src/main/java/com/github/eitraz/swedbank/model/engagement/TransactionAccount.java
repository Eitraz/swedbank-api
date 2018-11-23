package com.github.eitraz.swedbank.model.engagement;

import com.github.eitraz.swedbank.json.BalanceDeserializer;
import com.github.eitraz.swedbank.model.Links;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class TransactionAccount {
    private boolean selectedForQuickbalance;
    private Links links;
    private String name;
    private String priority;
    private String id;
    private String currency;
    private Details details;

    @JsonDeserialize(using = BalanceDeserializer.class)
    private Double balance;

    private String clearingNumber;
    private String accountNumber;
    private String fullyFormattedNumber;
    private boolean availableForFavouriteAccount;
    private boolean availableForPriorityAccount;
    private String originalName;
}

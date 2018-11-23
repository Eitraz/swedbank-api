package com.github.eitraz.swedbank.model.engagement.transactions;

import com.github.eitraz.swedbank.model.Links;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class Transactions {
    private String statementTimestamp;
    private Account account;
    private List<Transaction> transactions;
    private List<Transaction> reservedTransactions;
    private int uncategorizedExpenseTransactions;
    private int uncategorizedIncomeTransactions;
    private String uncategorizedSubcategoryId;
    private int uncategorizedSortOfReceivers;
    private boolean moreTransactionsAvailable;
    private int numberOfTransactions;
    private int numberOfReservedTransactions;
    private int numberOfBankGiroPrognosisTransactions;
    private List<Transaction> bankGiroPrognosisTransactions;
    private Links links;
}

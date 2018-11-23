package com.github.eitraz.swedbank.model.engagement;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class Overview {
    private List<TransactionAccount> transactionAccounts;
    private List<TransactionAccount> transactionDisposalAccounts;
    private List<TransactionAccount> loanAccounts;
    private List<TransactionAccount> savingAccounts;
    private List<TransactionAccount> cardAccounts;
    private List<TransactionAccount> supplementaryCardAccounts;
    private boolean accessToHSB;
    private boolean errorFetchingCreditCards;
    private List<DebitCard> debitCards;
    private boolean showCreditCardIncreaseLimitLink;
    private boolean eligibleForOverdraftLimit;
}

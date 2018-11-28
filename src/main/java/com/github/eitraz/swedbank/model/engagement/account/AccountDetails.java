package com.github.eitraz.swedbank.model.engagement.account;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.github.eitraz.swedbank.model.engagement.transactions.ExpenseControl;
import com.github.eitraz.swedbank.model.engagement.transactions.QuickbalanceSubscription;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class AccountDetails {
    private Name name;
    private String id;
    private Amount balance;
    private String clearingNumber;
    private String accountNumber;
    private DefaultFromAccount defaultFromAccount;
    private String fullyFormattedNumber;
    private Amount availableAmount;
    private Amount reservedAmount;
    private ExpenseControl expenseControl;
    private Amount creditGranted;

    @JsonAlias({"quickBalanceSubscription", "quickbalanceSubscription"})
    private QuickbalanceSubscription quickBalanceSubscription;
    private SpontaneousSave spontaneousSave;
    private TopupQuickBalance topupQuickBalance;
}

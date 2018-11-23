package com.github.eitraz.swedbank.model.engagement.transactions;

import com.github.eitraz.swedbank.model.Links;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class ExpenseControl {
    private String status;
    private boolean viewCategorizations;
    private Links links;
}

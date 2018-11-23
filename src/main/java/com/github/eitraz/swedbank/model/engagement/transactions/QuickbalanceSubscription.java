package com.github.eitraz.swedbank.model.engagement.transactions;

import com.github.eitraz.swedbank.model.Links;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class QuickbalanceSubscription {
    private String id;
    private boolean active;
    private Links links;
}

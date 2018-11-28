package com.github.eitraz.swedbank.model.engagement.account;

import com.github.eitraz.swedbank.model.Links;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class TopupQuickBalance {
    private boolean active;
    private Links links;
}

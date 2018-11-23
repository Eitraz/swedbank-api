package com.github.eitraz.swedbank.model.engagement;

import com.github.eitraz.swedbank.model.Links;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class DebitCard {
    private String id;
    private String productName;
    private Links links;
}

package com.github.eitraz.swedbank.model.engagement.transactions;

import com.github.eitraz.swedbank.model.Links;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class Label {
    private Links links;
    private String name;
    private String id;
    private String type;
}

package com.github.eitraz.swedbank.model.engagement.transactions;

import com.github.eitraz.swedbank.json.BalanceDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class Category {
    private String name;
    private String group;
    private String id;

    @JsonDeserialize(using = BalanceDeserializer.class)
    private Double amount;
}

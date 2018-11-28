package com.github.eitraz.swedbank.model.engagement.account;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.eitraz.swedbank.json.BalanceDeserializer;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class Amount {
    private String currencyCode;

    @JsonDeserialize(using = BalanceDeserializer.class)
    private Double amount;
}

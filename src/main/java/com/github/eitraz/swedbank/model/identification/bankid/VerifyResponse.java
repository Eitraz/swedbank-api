package com.github.eitraz.swedbank.model.identification.bankid;

import com.github.eitraz.swedbank.model.Links;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class VerifyResponse {
    private boolean extendedUsage;
    private IdentificationStatus status;
    private Links links;
    private String serverTime;
    private String formattedServerTime;
}

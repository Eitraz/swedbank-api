package com.github.eitraz.swedbank.model.identification.bankid;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum IdentificationStatus {
    @JsonProperty("USER_SIGN")
    USER_SIGN,

    @JsonProperty("COMPLETE")
    COMPLETE,

    @JsonProperty("CLIENT_NOT_STARTED")
    CLIENT_NOT_STARTED,

    @JsonEnumDefaultValue
    UNKNOWN
}

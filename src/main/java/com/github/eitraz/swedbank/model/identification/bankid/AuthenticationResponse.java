package com.github.eitraz.swedbank.model.identification.bankid;

import com.github.eitraz.swedbank.model.Links;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class AuthenticationResponse {
    private IdentificationStatus status;
    private Links links;
}

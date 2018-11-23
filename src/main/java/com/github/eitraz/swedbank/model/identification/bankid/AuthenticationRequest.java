package com.github.eitraz.swedbank.model.identification.bankid;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class AuthenticationRequest {
    private boolean useEasyLogin;
    private boolean generateEasyLoginId;
    private String userId;

    public static AuthenticationRequest create(String userId) {
        AuthenticationRequest loginRequest = new AuthenticationRequest();
        loginRequest.setUseEasyLogin(false);
        loginRequest.setGenerateEasyLoginId(false);
        loginRequest.setUserId(userId);
        return loginRequest;
    }
}

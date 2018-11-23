package com.github.eitraz.swedbank.model.profile;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class MenuGroupItem {
    private boolean authorized;
    private List<String> requestMethods;
    private List<String> serviceURIs;
    private boolean authorizedIfAuthMethodUpgrade;
}

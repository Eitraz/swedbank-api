package com.github.eitraz.swedbank.model.profile;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class Bank {
    private String name;
    private String url;
    private String bankId;
    private PrivateProfile privateProfile;
    private List<CorporateProfile> corporateProfiles;
}

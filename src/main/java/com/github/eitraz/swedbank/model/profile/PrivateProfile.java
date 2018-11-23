package com.github.eitraz.swedbank.model.profile;

import com.github.eitraz.swedbank.model.Links;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class PrivateProfile {
    private String activeProfileLanguage;
    private String id;
    private String url;
    private String targetType;
    private String bankId;
    private String customerNumber;
    private String bankName;
    private boolean customerInternational;
    private String customerName;
    private boolean youthProfile;
    private Links links;
    private List<CorporateProfile> corporateProfiles;
}

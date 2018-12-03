package com.github.eitraz.swedbank.model.profile;

import com.github.eitraz.swedbank.model.Links;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class CorporateProfile {
    private String activeProfileName;
    private String activeProfileLanguage;
    private String id;
    private String url;
    private String targetType;
    private String bankId;
    private String customerNumber;
    private String bankName;
    private String customerInternational;
    private String customerName;
    private Boolean youthProfile;
    private Links links;
}

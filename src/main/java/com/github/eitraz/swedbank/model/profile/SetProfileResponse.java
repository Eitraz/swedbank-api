package com.github.eitraz.swedbank.model.profile;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class SetProfileResponse {
    private PrivateProfile selectedProfile;
    private MenuStructure menuStructure;
    private Map<String, MenuItem> menuItems;
    private boolean mobile;
    private boolean nibprimaryUser;
    private String cacheGroup;
}

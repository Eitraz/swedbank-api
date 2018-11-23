package com.github.eitraz.swedbank.model.profile;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class MenuItem {
    private String name;
    private String uri;
    private String method;
    private String authorization;
}

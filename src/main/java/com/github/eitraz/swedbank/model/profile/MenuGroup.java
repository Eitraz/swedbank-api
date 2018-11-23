package com.github.eitraz.swedbank.model.profile;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class MenuGroup {
    private List<String> name;
    private List<MenuGroupItem> items;
}

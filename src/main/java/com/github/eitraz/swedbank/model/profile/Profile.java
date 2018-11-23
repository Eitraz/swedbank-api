package com.github.eitraz.swedbank.model.profile;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class Profile {
    private String userId;
    private boolean hasSwedbankProfile;
    private boolean hasSavingbankProfile;
    private List<Bank> banks;
}

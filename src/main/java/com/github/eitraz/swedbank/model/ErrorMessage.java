package com.github.eitraz.swedbank.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@SuppressWarnings({"WeakerAccess", "unused"})
@Getter
@Setter
public class ErrorMessage {
    private Map<String, Object> errorMessages;
}

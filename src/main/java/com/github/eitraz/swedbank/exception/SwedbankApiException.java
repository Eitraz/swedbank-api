package com.github.eitraz.swedbank.exception;

@SuppressWarnings("unused")
public class SwedbankApiException extends Exception {
    public SwedbankApiException(String message) {
        super(message);
    }

    public SwedbankApiException(String message, Throwable cause) {
        super(message, cause);
    }
}

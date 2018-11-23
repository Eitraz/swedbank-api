package com.github.eitraz.swedbank.exception;

@SuppressWarnings("unused")
public class SwedbankAuthenticationException extends Exception {
    public SwedbankAuthenticationException(String message) {
        super(message);
    }

    public SwedbankAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}

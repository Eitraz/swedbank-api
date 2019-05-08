package com.github.eitraz.swedbank.bank;

@SuppressWarnings("unused")
public enum BankType {
    SWEDBANK("B7dZHQcY78VRVz9l", "SwedbankMOBPrivateIOS/4.9.2_(iOS;_10.0.2)_Apple/iPhone7,2");

    private final String id;
    private final String userAgent;

    BankType(String id, String userAgent) {
        this.id = id;
        this.userAgent = userAgent;
    }

    public String getId() {
        return id;
    }

    public String getUserAgent() {
        return userAgent;
    }
}

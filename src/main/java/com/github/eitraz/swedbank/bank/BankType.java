package com.github.eitraz.swedbank.bank;

@SuppressWarnings("unused")
public enum BankType {
    SWEDBANK("U2wuZNZTj3mEfZIL", "SwedbankMOBPrivateIOS/4.9.2_(iOS;_10.0.2)_Apple/iPhone7,2"),
    SPARBANKEN("xZaJaAruXVsQ7nu1", "SavingbankMOBPrivateIOS/4.9.2_(iOS;_10.0.2)_Apple/iPhone7,2"),
    SWEDBANK_UNG("vEgFFWmbG1IbipQs", "SwedbankMOBYouthIOS/2.4.2_(iOS;_10.0.2)_Apple/iPhone7,2"),
    SPARBANKEN_UNG("QmaEk0DY4w2I9xCb", "SavingbankMOBYouthIOS/2.4.2_(iOS;_10.0.2)_Apple/iPhone7,2"),
    SWEDBANK_FORETAG("FhhzLma2LS7mjzfG", "SwedbankMOBCorporateIOS/2.6.2_(iOS;_10.0.2)_Apple/iPhone7,2"),
    SPARBANKEN_FORETAG("BfKTExxSfEg1ONnp", "SavingbankMOBCorporateIOS/2.6.2_(iOS;_10.0.2)_Apple/iPhone7,2");

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

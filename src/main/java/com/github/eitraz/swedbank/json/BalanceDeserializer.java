package com.github.eitraz.swedbank.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class BalanceDeserializer extends JsonDeserializer<Double> {
    @Override
    public Double deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        String balance = jsonParser.getValueAsString();

        if (balance == null)
            return null;

        try {
            return format.parse(balance.replace(" ", "")).doubleValue();
        } catch (ParseException e) {
            throw new IOException(e);
        }
    }
}

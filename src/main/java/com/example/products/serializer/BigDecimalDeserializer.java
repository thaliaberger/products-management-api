package com.example.products.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {
    @Override
    public BigDecimal deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {
        String priceString = jsonParser.getText();
        return new BigDecimal(priceString.replaceAll("[^\\d.]", ""));
    }
}

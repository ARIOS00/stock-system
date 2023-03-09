package com.example.stock.serialization;

import com.example.stock.entity.Amount;
import com.example.stock.entity.Order;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class AmountSerialize extends JsonSerializer<Amount> {
    @Override
    public void serialize(Amount amount, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("@class", amount.getClass().getName());

        jsonGenerator.writeStringField("id", amount.getName());
        jsonGenerator.writeStringField("stockName", amount.getAmount().toString());

        jsonGenerator.writeEndObject();
    }

}

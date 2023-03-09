package com.example.stock.serialization;

import com.example.stock.entity.Order;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class OrderSerialize extends JsonSerializer<Order> {

    @Override
    public void serialize(Order order, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("@class", order.getClass().getName());

        jsonGenerator.writeStringField("id", order.getId().toString());
        jsonGenerator.writeStringField("stockName", order.getStockName());
        jsonGenerator.writeStringField("userId", order.getUserId().toString());
        jsonGenerator.writeStringField("email", order.getEmail());
        jsonGenerator.writeStringField("enable", order.getEnable().toString());

        jsonGenerator.writeEndObject();
    }
}

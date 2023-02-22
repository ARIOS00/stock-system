package com.example.stock.serialization;

import com.example.stock.entity.Trade;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class TradeSerialize extends JsonSerializer<Trade> {

    @Override
    public void serialize(Trade trade, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("@class", trade.getClass().getName());

        jsonGenerator.writeStringField("name", trade.getName());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jsonGenerator.writeStringField("freshTime", simpleDateFormat.format(trade.getFreshTime()));
        jsonGenerator.writeStringField("price", trade.getPrice().toString());
        jsonGenerator.writeStringField("diff", trade.getDiff().toString());
        jsonGenerator.writeStringField("rate", trade.getRate().toString());

        jsonGenerator.writeEndObject();
    }
}

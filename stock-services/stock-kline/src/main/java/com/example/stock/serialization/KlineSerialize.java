package com.example.stock.serialization;

import com.example.stock.entity.Kline;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class KlineSerialize extends JsonSerializer<Kline> {

    @Override
    public void serialize(Kline kline, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("name", kline.getName().toString());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        jsonGenerator.writeStringField("kdate", simpleDateFormat.format(kline.getKdate()));
        jsonGenerator.writeStringField("close", kline.getClose().toString());
        jsonGenerator.writeStringField("volume", kline.getVolume().toString());
        jsonGenerator.writeStringField("open", kline.getOpen().toString());
        jsonGenerator.writeStringField("high", kline.getHigh().toString());
        jsonGenerator.writeStringField("low", kline.getLow().toString());

        jsonGenerator.writeEndObject();
    }
}

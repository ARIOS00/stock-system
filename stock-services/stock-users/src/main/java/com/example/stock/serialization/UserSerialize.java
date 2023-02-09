package com.example.stock.serialization;

import com.example.stock.entity.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class UserSerialize extends JsonSerializer<User> {

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("id", user.getId().toString());
        jsonGenerator.writeStringField("nickname", user.getNickname());
        jsonGenerator.writeStringField("password", user.getPassword());
        jsonGenerator.writeStringField("salt", user.getSalt());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jsonGenerator.writeStringField("registerDate", simpleDateFormat.format(user.getRegisterDate()));
        jsonGenerator.writeStringField("lastLoginDate", simpleDateFormat.format(user.getLastLoginDate()));

        jsonGenerator.writeStringField("loginCount", user.getLoginCount().toString());

        jsonGenerator.writeEndObject();
    }
}

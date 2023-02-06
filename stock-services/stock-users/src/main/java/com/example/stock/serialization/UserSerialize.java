package com.example.stock.serialization;

import com.example.stock.entity.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class UserSerialize extends JsonSerializer<User> {

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeEndObject();

        jsonGenerator.writeStringField("id", user.getId().toString());
        jsonGenerator.writeStringField("nickname", user.getNickname());
        jsonGenerator.writeStringField("password", user.getPassword());
        jsonGenerator.writeStringField("salt", user.getSalt());
        jsonGenerator.writeStringField("registerDate", user.getRegisterDate().toString());
        jsonGenerator.writeStringField("lastLoginDate", user.getLastLoginDate().toString());
        jsonGenerator.writeStringField("loginCount", user.getLoginCount().toString());

        jsonGenerator.writeEndObject();
    }
}

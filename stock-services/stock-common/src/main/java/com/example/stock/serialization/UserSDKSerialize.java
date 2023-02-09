package com.example.stock.serialization;

import com.example.stock.vo.UserSDK;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class UserSDKSerialize extends JsonSerializer<UserSDK> {

    @Override
    public void serialize(UserSDK userSDK, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("id", userSDK.getId().toString());
        jsonGenerator.writeStringField("nickname", userSDK.getNickname());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jsonGenerator.writeStringField("registerDate", simpleDateFormat.format(userSDK.getRegisterDate()));
        jsonGenerator.writeStringField("lastLoginDate", simpleDateFormat.format(userSDK.getLastLoginDate()));
        jsonGenerator.writeStringField("loginCount", userSDK.getLoginCount().toString());

        jsonGenerator.writeEndObject();
    }
}

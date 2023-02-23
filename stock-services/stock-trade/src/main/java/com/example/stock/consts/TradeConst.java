package com.example.stock.consts;

import org.springframework.beans.factory.annotation.Value;

public class TradeConst {

    public static final String TOPIC = "trade";

    @Value("${spring.kafka.consumer.initial_offset}")
    public static String INITIAL_OFFSET;

}

package com.example.stock.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

public interface IKafkaService {
    void ConsumeTradeKafkaMessage(ConsumerRecord<?, ?> record);
}

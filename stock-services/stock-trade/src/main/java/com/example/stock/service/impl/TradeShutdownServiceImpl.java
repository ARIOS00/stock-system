package com.example.stock.service.impl;

import com.example.stock.consts.TradeConst;
import com.example.stock.service.ITradeShutdownService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Slf4j
@Service
public class TradeShutdownServiceImpl implements ITradeShutdownService {

    private KafkaListenerEndpointRegistrar registrar;

    private final ConsumerFactory<Object, Object> consumerFactory;

    @Autowired
    public TradeShutdownServiceImpl(ConsumerFactory<Object, Object> consumerFactory) {
        this.consumerFactory = consumerFactory;
    }

    @Override
    public void onDestroy() {
        System.out.println("aaaaaaa");
        MessageListenerContainer container = registrar.getEndpointRegistry().getListenerContainer("ConsumerGroup-trade-1");

        if(container != null && !container.isContainerPaused())
            container.pause();

        if(container!= null && container.isRunning())
            container.stop();

        Consumer<Object, Object> consumer = consumerFactory.createConsumer("offset_submitter", null);
        consumer.seek(new TopicPartition(TradeConst.TOPIC, 0), 60);
        consumer.commitSync();
        System.out.println("bbbbbbb");
    }
}

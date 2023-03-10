package com.example.stock.service.impl;

import com.example.stock.consts.TradeConst;
import com.example.stock.dao.TradeDao;
import com.example.stock.entity.Trade;
import com.example.stock.schedule.Submitter;
import com.example.stock.service.IKafkaService;
import com.example.stock.util.DataSavePipelineThread;
import com.example.stock.util.TradeUtil;
import com.example.stock.vo.TradeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class KafkaServiceImpl implements IKafkaService {

    private final KafkaListenerEndpointRegistry registry;
    private final ConsumerFactory<Object, Object> consumerFactory;
    private final TradeDao tradeDao;
    private final RedisTemplate redisTemplate;
    private final Submitter submitter;

    private List<DataSavePipelineThread> pipelines;


    @Autowired
    public KafkaServiceImpl(KafkaListenerEndpointRegistry registry, ConsumerFactory<Object, Object> consumerFactory, TradeDao tradeDao, RedisTemplate redisTemplate, Submitter submitter) {
        this.registry = registry;
        this.consumerFactory = consumerFactory;
        this.tradeDao = tradeDao;
        this.redisTemplate = redisTemplate;
        this.submitter = submitter;

        this.pipelines = new LinkedList<>();
        for(int i = 0; i < TradeConst.THREAD_NUM; i++) {
            pipelines.add(new DataSavePipelineThread(tradeDao, redisTemplate, submitter));
        }
    }
    @Override
    @KafkaListener(id = "stock-trade-1", topicPartitions =
            { @org.springframework.kafka.annotation.TopicPartition(topic = "trade", partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "${spring.kafka.initial-offset}"))})
    public void consumeTradeKafkaMessage(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if(!kafkaMessage.isPresent())
            return;
        String message = kafkaMessage.get().toString();
        log.info("Receive CouponKafkaMessage: {}, offset: {}", message, record.offset());
        try {
            Trade trade = TradeUtil.StringToTrade(message);
            TradeMessage tradeMessage = new TradeMessage();
            tradeMessage.setTrade(trade);
            tradeMessage.setOffset(record.offset());
            int threadId = (int) (record.offset() % TradeConst.THREAD_NUM);
            DataSavePipelineThread pipeline = pipelines.get(threadId);
            pipeline.add(tradeMessage);
            if(!pipeline.isAlive())
                pipeline.start();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("message conversion failed!");
        }
    }
}

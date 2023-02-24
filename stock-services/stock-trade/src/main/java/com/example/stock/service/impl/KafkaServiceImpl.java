package com.example.stock.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.stock.consts.TradeConst;
import com.example.stock.dao.TradeDao;
import com.example.stock.entity.Trade;
import com.example.stock.service.IKafkaService;
import com.example.stock.util.DataSavePipelineThread;
import com.example.stock.util.TradeUtil;
import com.example.stock.vo.TradeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class KafkaServiceImpl implements IKafkaService {

    private final KafkaListenerEndpointRegistry registry;
    private final ConsumerFactory<Object, Object> consumerFactory;
    private final TradeDao tradeDao;

    private List<DataSavePipelineThread> pipelines;

    @Autowired
    private ConfigurableEnvironment environment;

    @Autowired
    public KafkaServiceImpl(KafkaListenerEndpointRegistry registry, ConsumerFactory<Object, Object> consumerFactory, TradeDao tradeDao) {
        this.registry = registry;
        this.consumerFactory = consumerFactory;
        this.tradeDao = tradeDao;

        this.pipelines = new LinkedList<>();
        for(int i = 0; i < TradeConst.THREAD_NUM; i++) {
            pipelines.add(new DataSavePipelineThread(tradeDao));
            pipelines.get(i).start();
        }
    }
    @Override
    @KafkaListener(id = "stock-trade-1", topicPartitions =
            { @org.springframework.kafka.annotation.TopicPartition(topic = "trade", partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "${spring.kafka.initial-offset}"))})
    public void ConsumeTradeKafkaMessage(ConsumerRecord<?, ?> record) {
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

            pipelines.get((int) (record.offset() % TradeConst.THREAD_NUM)).add(tradeMessage);
        } catch (Exception e) {
            log.error("save failed!");
        }
    }

    @Scheduled(initialDelay = 2000, fixedRate = 3000)
    public void ini() {
//        String src = "D:\\Full_Stack_Projects\\stock-system\\stock-services\\stock-trade\\src\\main\\resources\\application.yml";
//        Yaml yaml = new Yaml();
//        FileWriter fileWriter = null;
//
//        Map<String, Object> springMap, dataSourceMap, resultMap,helperDialect;
//        try {
//            resultMap = (Map<String, Object>) yaml.load(new FileInputStream(new File(src)));
//            springMap = (Map<String, Object>) resultMap.get("spring");
//            dataSourceMap = (Map<String, Object>) springMap.get("kafka");
//
//            dataSourceMap.put("initial-offset", "100");
//
//            fileWriter = new FileWriter(new File(src));
//            fileWriter.write(yaml.dumpAsMap(resultMap));
//
//            fileWriter.flush();
//            fileWriter.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("yml modifying failed!");
//        }

    }

}

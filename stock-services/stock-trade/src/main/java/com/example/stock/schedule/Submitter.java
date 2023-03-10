package com.example.stock.schedule;

import com.alibaba.fastjson.JSON;
import com.example.stock.consts.TradeConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class Submitter {
    private PriorityQueue<Long> priorityQueue;

    private final Lock lock;

    @Value("${spring.kafka.initial-offset}")
    private Long offset;

    @Autowired
    public Submitter() {
        this.priorityQueue = new PriorityQueue<>((num1, num2) -> (int) (num1 - num2));
        this.lock = new ReentrantLock();
    }

    @Scheduled(initialDelay = 2000, fixedDelay = 7000)
    public void submit() {
        lock.lock();
        try {
            log.info("try to submit the offset, priority queue: {}", JSON.toJSONString(priorityQueue));
            if(CollectionUtils.isEmpty(priorityQueue))
                return;
            if(null == offset)
                offset = priorityQueue.peek();
            while(!CollectionUtils.isEmpty(priorityQueue)) {
                if(priorityQueue.peek() - offset > 1)
                    break;
                offset = priorityQueue.poll();
            }
            String initialOffset = offset + 1 + "";
            String src = TradeConst.YAML_PATH;
            Yaml yaml = new Yaml();
            FileWriter fileWriter = null;

            Map<String, Object> springMap, dataSourceMap, resultMap, helperDialect;

            resultMap = (Map<String, Object>) yaml.load(new FileInputStream(new File(src)));
            springMap = (Map<String, Object>) resultMap.get("spring");
            dataSourceMap = (Map<String, Object>) springMap.get("kafka");

            dataSourceMap.put("initial-offset", initialOffset);

            fileWriter = new FileWriter(new File(src));
            fileWriter.write(yaml.dumpAsMap(resultMap));

            fileWriter.flush();
            fileWriter.close();

            log.info("initial offset submitted: {}", initialOffset);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("yml modifying failed!");
        } finally {
            lock.unlock();
        }
    }

    public void add(Long num) {
        lock.lock();
        priorityQueue.add(num);
        lock.unlock();
    }
}

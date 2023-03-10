package com.example.stock.util;

import com.example.stock.dao.TradeDao;
import com.example.stock.entity.Trade;
import com.example.stock.entity.TradeDefault;
import com.example.stock.schedule.Submitter;
import com.example.stock.vo.TradeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.LinkedList;
import java.util.Queue;

@Slf4j
public class DataSavePipelineThread extends Thread{

    private TradeDao tradeDao;
    private RedisTemplate redisTemplate;
    private Submitter submitter;

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Queue<TradeMessage> memoryQueue;

    public DataSavePipelineThread(TradeDao tradeDao, RedisTemplate redisTemplate, Submitter submitter) {
        this.memoryQueue = new LinkedList<>();
        this.tradeDao = tradeDao;
        this.redisTemplate = redisTemplate;
        this.submitter = submitter;
    }
    @Override
    public void run() {
        if(null == memoryQueue || CollectionUtils.isEmpty(memoryQueue))
            return;
        while(!CollectionUtils.isEmpty(memoryQueue)) {
            TradeMessage msg = memoryQueue.poll();
            if(null == msg)
                continue;
            try {
                Trade trade = msg.getTrade();
                if(null == trade)
                    continue;
                redisTemplate.opsForValue().set("trade:" + trade.getName() + ":" + format.format(trade.getFreshTime()), new TradeDefault(trade), Duration.ofDays(1));
                tradeDao.save(trade);
                log.info("{}, offset: {}: completed!, memoryQueue {} remain", Thread.currentThread().getName(), msg.getOffset(), memoryQueue.size());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                submitter.add(msg.getOffset());
            }
        }
    }

//    @Override
//    public void run() {
//        while(true) {
//            try {
//                Thread.sleep(1);
//            } catch (Exception e) {
//                continue;
//            }
//
//            if(null == memoryQueue || CollectionUtils.isEmpty(memoryQueue))
//                continue;
//            TradeMessage msg = memoryQueue.poll();
//            try {
//                if(null == msg)
//                    continue;
//
//                Trade trade = msg.getTrade();
//                if(null == trade)
//                    continue;
//                redisTemplate.opsForValue().set("trade:" + trade.getName() + ":" + format.format(trade.getFreshTime()), new TradeDefault(trade), Duration.ofDays(1));
//                tradeDao.save(trade);
//                log.info(Thread.currentThread().getName() + ", offset: " + msg.getOffset() + ": "+ "completed!");
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                submitter.add(msg.getOffset());
//            }
//        }
//    }

    public void add(TradeMessage msg) {
        memoryQueue.offer(msg);
    }
}

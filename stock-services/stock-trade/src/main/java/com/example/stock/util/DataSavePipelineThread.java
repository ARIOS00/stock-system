package com.example.stock.util;

import com.example.stock.dao.TradeDao;
import com.example.stock.entity.Trade;
import com.example.stock.vo.TradeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.Queue;

@Slf4j
@Component
public class DataSavePipelineThread extends Thread{

    private final TradeDao tradeDao;
    private Queue<TradeMessage> memoryQueue;

    @Autowired
    public DataSavePipelineThread(TradeDao tradeDao) {
        this.memoryQueue = new LinkedList<>();
        this.tradeDao = tradeDao;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(1);
                if(null == memoryQueue || CollectionUtils.isEmpty(memoryQueue))
                    continue;
                TradeMessage msg = memoryQueue.poll();

                if(null == msg)
                    continue;

                Trade trade = msg.getTrade();
                if(null == trade)
                    continue;
                tradeDao.save(trade);
                System.out.println(Thread.currentThread().getName() + ", offset: " + msg.getOffset() + ": "+ "completed!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void add(TradeMessage msg) {
        memoryQueue.offer(msg);
    }
}

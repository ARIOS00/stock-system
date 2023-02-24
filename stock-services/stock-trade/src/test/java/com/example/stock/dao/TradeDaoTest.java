package com.example.stock.dao;

import com.alibaba.fastjson.JSON;
import com.example.stock.entity.Trade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TradeDaoTest {
    @Autowired
    private TradeDao tradeDao;

    @Test
    public void testSaveAndGet() {
        List<Trade> trades = tradeDao.findTradesByName("AAAA");
        Trade t = trades.get(0);

        tradeDao.save(t);
    }

    @Test
    public void pq() {
        PriorityQueue<Integer> queue = new PriorityQueue<>((num1, num2) -> num1 - num2);
        queue.add(2);
        queue.add(5);
        queue.add(1);
        queue.add(9);
        queue.add(0);
        while(queue.size() != 0){
            System.out.println(queue.poll());
        }

    }

    private Trade mockTrade() {
        Trade trade = new Trade();
        trade.setName("TEST");
        trade.setFreshTime(new Date());
        trade.setPrice(100.0);
        trade.setDiff(1.0);
        trade.setRate(0.2);
        return trade;
    }
}

package com.example.stock.dao;

import com.alibaba.fastjson.JSON;
import com.example.stock.entity.Trade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TradeDaoTest {
    @Autowired
    private TradeDao tradeDao;

    @Test
    public void testSaveAndGet() throws ParseException {
        Trade trade = tradeDao.findTradeByNameAndFreshTime("GOOG", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2023-02-24 12:59:00"));
        System.out.println(JSON.toJSONString(trade));
        List<Trade> prices = tradeDao.findTradesByName("AMZN");
        System.out.println(JSON.toJSONString(prices));
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

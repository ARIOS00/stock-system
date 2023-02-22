package com.example.stock.dao;

import com.example.stock.entity.Trade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TradeDaoTest {
    @Autowired
    private TradeDao tradeDao;

    @Test
    public void testSaveAndGet() {
        List<Trade> trades = tradeDao.findTradesByName("AAAA");
        trades.get(0).setName("AAA");
        tradeDao.save(mockTrade());
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

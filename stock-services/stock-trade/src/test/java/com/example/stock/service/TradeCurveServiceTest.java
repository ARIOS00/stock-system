package com.example.stock.service;

import com.alibaba.fastjson.JSON;
import com.example.stock.entity.Trade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TradeCurveServiceTest {

    @Autowired
    private ITradeCurveService tradeCurveService;
    @Test
    public void testGetTradeCurveByName() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(tradeCurveService.getTradeCurveByName("AMZN"));
        Thread.sleep(3000);
    }
}

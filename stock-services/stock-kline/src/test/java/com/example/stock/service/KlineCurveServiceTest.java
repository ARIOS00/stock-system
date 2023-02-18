package com.example.stock.service;

import com.example.stock.entity.Kline;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.AssertTrue;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KlineCurveServiceTest {
    @Autowired
    private IKlineCurveService klineCurveService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testGetLatestKlineByName() throws Exception{
        String name = "AMZN";
        Kline kline = klineCurveService.getLatestKlineByName(name);
        Kline klineRedis = new Kline();
        String dateStr = redisTemplate.opsForValue().get("latest_kline_date");
        klineRedis.getKline(redisTemplate.opsForHash().entries("kline:" + name + ":" + dateStr));
        Assert.assertTrue(kline.equals(klineRedis));
    }
}

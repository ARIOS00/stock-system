package com.example.stock.service;

import com.alibaba.fastjson.JSON;
import com.example.stock.dao.KlineDao;
import com.example.stock.entity.Kline;
import com.example.stock.entity.KlineDefault;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.AssertTrue;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KlineCurveServiceTest {
    @Autowired
    private IKlineCurveService klineCurveService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private KlineDao klineDao;

    @Test
    public void testtt() {
        List<String> list = new LinkedList<>();
        list.add(null);
        list.add(null);
        System.out.println(list.indexOf(null));
    }

    @Test
    public void testSave() throws Exception {
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2022-09-23");
        Kline kline = klineDao.findKlineByNameAndKdate("AMZN", startDate);
        System.out.println(JSON.toJSONString(kline));
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(kline.getKdate()));
        KlineDefault klineDefault = new KlineDefault(kline);
        redisTemplate.opsForValue().set("test", klineDefault);
        Kline klineRedis = (KlineDefault) redisTemplate.opsForValue().get("test");
        System.out.println(JSON.toJSONString(klineRedis));
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(klineRedis.getKdate()));
        Assert.assertTrue(kline.equals(klineRedis));
    }

    @Test
    public void testGetLatestKlineByName() throws Exception{
        String name = "AMZN";
        Kline kline = klineCurveService.getLatestKlineByName(name);
        System.out.println(kline);
    }

    @Test
    public void testGetKlineCurveByNameAndDuration() throws Exception{
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2022-09-23");
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2023-02-7");
        List<Kline> curve = klineCurveService.getKlineCurveByNameAndDuration("AMZN", startDate, endDate);
        System.out.println(JSON.toJSONString(curve));
    }

    private Kline redisGet(String name) throws Exception {
//        Kline klineRedis = new Kline();
//        String dateStr = redisTemplate.opsForValue().get("latest_kline_date");
//        klineRedis.getKline(redisTemplate.opsForHash().entries("kline:" + name + ":" + dateStr));
//        return klineRedis;
        return null;
    }
}

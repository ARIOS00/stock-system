package com.example.stock.dao;

import com.alibaba.fastjson.JSON;
import com.example.stock.entity.Kline;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KlineDaoTest {
    @Autowired
    private KlineDao klineDao;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    public void testKlineDaoSave() {
        List<Kline> klineTSLA =  klineDao.findKlinesByName("TSLA");
        System.out.println(JSON.toJSONString(klineTSLA));
    }

    @Test
    public void testKlineDaoFindAllNames() {
        Set<String> names = klineDao.findAllNames();
        System.out.println(JSON.toJSONString(names));
    }

    @Test
    public void testKlineSaveToRedis() throws ParseException {
        Kline kline = createKline();
        redisTemplate.opsForHash().putAll("testMETA", kline.getMap());
        Kline qKline = new Kline();
        qKline = qKline.getKline(redisTemplate.opsForHash().entries("testMETA"));
        redisTemplate.delete("testMETA");
        Assert.assertTrue(kline.equals(qKline));
    }

    private Kline createKline() throws ParseException {
        Kline kline = new Kline();
        kline.setName("META");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        date = dateFormat.parse(dateFormat.format(date));
        kline.setKdate(date);
        kline.setVolume(121425.22);
        kline.setClose(145.1);
        kline.setOpen(231.2);
        kline.setHigh(124.3);
        kline.setLow(111.5);
        return kline;
    }
}

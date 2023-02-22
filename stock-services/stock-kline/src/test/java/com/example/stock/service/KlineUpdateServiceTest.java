package com.example.stock.service;

import com.alibaba.fastjson.JSON;
import com.example.stock.entity.Kline;
import com.example.stock.service.impl.KlineUpdateServiceImpl;
import com.example.stock.vo.UserSDK;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KlineUpdateServiceTest {
    @Autowired
    private KlineUpdateServiceImpl klineUpdateService;
    @Test
    public void testSplitList() {
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12);
        List<List<Integer>> res = new ArrayList<>();
        klineUpdateService.splitList(list, res);
        System.out.println(JSON.toJSONString(res));
    }

    @Test
    public void testKlineCurveUpdate() throws Exception {
        String name = "test";
        List<Kline> klines = new LinkedList<>();
        for(int i = 0; i < 100; i++) {
            klines.add(mockKline(name));
        }
        System.out.println("klines: " + klines.size());
        klineUpdateService.klineCurveUpdate(klines, mockUserSDK());
        TimeUnit.SECONDS.sleep(5L);
    }

    private UserSDK mockUserSDK() {
        UserSDK userSDK = new UserSDK();
        userSDK.setNickname("Jack");
        userSDK.setId(12341251L);
        return userSDK;
    }

    private Kline mockKline(String name) {
        Kline kline = new Kline();
        kline.setName(name);
        kline.setOpen(1.0);
        kline.setLow(1.0);
        kline.setHigh(1.0);
        kline.setKdate(new Date());
        kline.setClose(1.0);
        kline.setVolume(1.0);
        return kline;
    }
}

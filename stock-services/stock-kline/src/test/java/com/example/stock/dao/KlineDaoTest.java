package com.example.stock.dao;

import com.alibaba.fastjson.JSON;
import com.example.stock.entity.Kline;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KlineDaoTest {
    @Autowired
    private KlineDao klineDao;

    @Test
    public void testKlineDao() {
        List<Kline> klineTSLA =  klineDao.findKlinesByName("TSLA");
        System.out.println(klineTSLA.get(0).getKdate());
    }
}

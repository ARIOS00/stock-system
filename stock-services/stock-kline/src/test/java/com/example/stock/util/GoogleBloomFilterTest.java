package com.example.stock.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GoogleBloomFilterTest {
    @Autowired
    GoogleBloomFilter bloomFilter;

    @Test
    public void testBloomFilter() throws ParseException {
        Assert.assertTrue(bloomFilter.nameIsExist("TSLA"));
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2023-02-10");
        System.out.println(bloomFilter.dateIsExist(date));
    }
}

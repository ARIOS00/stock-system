package com.example.stock.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GoogleBloomFilterTest {
    @Autowired
    GoogleBloomFilter bloomFilter;

    @Test
    public void testBloomFilter() {
        Assert.assertTrue(bloomFilter.isExist("TSLA"));
    }
}

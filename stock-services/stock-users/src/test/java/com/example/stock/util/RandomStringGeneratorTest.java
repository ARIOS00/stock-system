package com.example.stock.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RandomStringGeneratorTest {
    @Test
    public void testGet() throws Exception {
        for(int i = 0; i < 10; i++) {
            System.out.println(RandomStringGenerator.get(MD5Util.SALT_LENGTH));
        }
    }
}

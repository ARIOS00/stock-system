package com.example.stock.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MD5UtilTest {
    @Test
    public void testInputPassToDBPass() throws Exception {
        System.out.println(MD5Util.inputPassToDBPass("123456", "sa14asv3556s"));
        System.out.println(new Date());
    }
}

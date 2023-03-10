package com.example.stock.service;

import com.alibaba.fastjson.JSON;
import com.example.stock.entity.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceTest {
    @Autowired
    private IOrderService orderService;

    @Test
    public void testCreateOrder() throws Exception {
        Order order = orderService.createOrder("AMZN", 555L, "kkkkkkkks@agg.com");
        System.out.println(JSON.toJSONString(order));
    }
}

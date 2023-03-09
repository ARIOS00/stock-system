package com.example.stock.dao;

import com.alibaba.fastjson.JSON;
import com.example.stock.entity.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderDaoTest {
    @Autowired
    private OrderDao orderDao;

    @Test
    public void testOrderDao() {
        Order order01 = new Order();
        order01.setStockName("AMZN");
        order01.setUserId(12345L);
        order01.setEmail("adsvv@asd.com");
        order01.setEnable(true);

        Order order02 = new Order();
        order02.setStockName("AMZN");
        order02.setUserId(111111L);
        order02.setEmail("adsvv@asd.com");
        order02.setEnable(true);

        orderDao.save(order01);
        orderDao.save(order02);

        List<Order> orders = orderDao.findAllByStockName("AMZN");
        System.out.println(JSON.toJSONString(orders));

        orders = orderDao.findAllByUserId(12345L);
        System.out.println(JSON.toJSONString(orders));
    }
}

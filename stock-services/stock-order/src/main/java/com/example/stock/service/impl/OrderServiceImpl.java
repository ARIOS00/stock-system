package com.example.stock.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.stock.dao.AmountDao;
import com.example.stock.dao.OrderDao;
import com.example.stock.entity.Order;
import com.example.stock.exception.OrderException;
import com.example.stock.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {

    private OrderDao orderDao;

    private AmountDao amountDao;

    private RedisTemplate redisTemplate;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, AmountDao amountDao, RedisTemplate redisTemplate) {
        this.orderDao = orderDao;
        this.amountDao = amountDao;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Order createOrder(String name, Long userId, String email) {
        if(StringUtils.isEmpty(name) || null == userId || null == email) {
            log.warn("empty stock name, user id, or email!");
            return null;
        }

        String key = "order:" + name + ":" + email;
        Order existOrder = queryOrderByEmailAndStockName(email, name, key);
        if(null != existOrder) {
            log.warn("stock: " + name + " subscription with the email: " + email + " already exist!");
            return null;
        }

        Order order = new Order();
        order.setStockName(name);
        order.setUserId(userId);
        order.setEmail(email);
        order.setEnable(true);

        return save(order, key);
    }

    private Order queryOrderByEmailAndStockName(String email, String stockName, String key) {
        Order order = (Order) redisTemplate.opsForValue().get(key);
        if(order != null)
            return order;

        order = orderDao.findByEmailAndStockName(email, stockName);
        if(order == null)
            redisTemplate.opsForValue().set(key, new Order());
        else
            redisTemplate.opsForValue().set(key, order);

        return order;
    }

    private Order save(Order order, String key) {
        order = orderDao.save(order);
        redisTemplate.opsForValue().set(key, order);
        return order;
    }
}

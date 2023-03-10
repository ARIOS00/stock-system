package com.example.stock.service.impl;

import com.example.stock.dao.AmountDao;
import com.example.stock.dao.OrderDao;
import com.example.stock.entity.Order;
import com.example.stock.exception.OrderException;
import com.example.stock.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
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
    public Order createOrder(String name, Long userId, String email) throws OrderException {
        if(StringUtils.isEmpty(name) || null == userId || null == email)
            throw new OrderException("empty stock name, user id, or email!");

        String key = "order:" + name + ":" + userId + "_" + email;
        Order order = queryOrderByEmailAndStockName(email, name, key);
        if(null != order)
            throw new OrderException("stock: " + name + "subscription with the email: " + email + " already exist!");

        order.setStockName(name);
        order.setUserId(userId);
        order.setEmail(email);
        order.setEnable(true);


    }

    private Order queryOrderByEmailAndStockName(String email, String stockName, String key) {
        Order order = (Order) redisTemplate.opsForValue().get(key);
        if(order != null) {
            if(order.getId() == null) {
                log.warn("empty query for Order cache penetration detected! key: {}", key);
                return null;
            }
            return order;
        }

        order = orderDao.findByEmailAndStockName(email, stockName);
        if(order == null)
            redisTemplate.opsForValue().set(key, new Order());
        return order;
    }

    private void subscribe(String stockName) {
        String key =
    }
}

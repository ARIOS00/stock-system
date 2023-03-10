package com.example.stock.service;

import com.example.stock.entity.Order;
import com.example.stock.exception.OrderException;

public interface IOrderService {
    Order createOrder(String name, Long userId, String email);
}

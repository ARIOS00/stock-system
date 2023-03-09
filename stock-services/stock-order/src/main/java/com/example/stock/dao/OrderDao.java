package com.example.stock.dao;

import com.example.stock.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDao extends JpaRepository<Order, Integer> {
    List<Order> findAllByStockName(String stockName);

    List<Order> findAllByUserId(Long userId);
}

package com.example.stock.dao;

import com.example.stock.entity.Amount;
import com.example.stock.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmountDao extends JpaRepository<Amount, Integer> {
    Amount findAmountByName(String name);
}

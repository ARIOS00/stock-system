package com.example.stock.dao;

import com.example.stock.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeDao extends JpaRepository<Trade, Integer> {
    public List<Trade> findTradesByName(String name);
}

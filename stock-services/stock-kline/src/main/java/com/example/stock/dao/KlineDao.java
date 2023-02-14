package com.example.stock.dao;

import com.example.stock.entity.Kline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KlineDao extends JpaRepository<Kline, Integer> {
    List<Kline> findKlinesByName(String name);
}

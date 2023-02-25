package com.example.stock.dao;

import com.example.stock.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface TradeDao extends JpaRepository<Trade, Integer> {
    List<Trade> findTradesByName(String name);

    Trade findTradeByNameAndFreshTime(String name, Date freshTime);

    @Query("SELECT t.name FROM Trade t")
    Set<String> findAllNames();

    @Query("SELECT t.freshTime FROM Trade t")
    Set<Date> findAllFreshTimes();
}

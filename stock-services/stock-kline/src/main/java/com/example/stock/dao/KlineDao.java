package com.example.stock.dao;

import com.example.stock.entity.Kline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface KlineDao extends JpaRepository<Kline, Integer> {
    List<Kline> findKlinesByName(String name);

    Kline findKlineByNameAndKdate(String name, Date kdate);

    @Query("SELECT k.name FROM Kline k")
    Set<String> findAllNames();

    @Query("SELECT k.kdate FROM Kline k")
    Set<Date> findAllDates();
}
package com.example.stock.service;

import com.example.stock.entity.Kline;

import java.util.Date;

public interface IKlineCurveService {
    Kline getKlineByNameAndDate(String name, Date date);
}

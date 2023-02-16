package com.example.stock.service;

import com.example.stock.entity.Kline;
import com.example.stock.exception.KlineException;

import java.util.Date;
import java.util.List;

public interface IKlineCurveService {
    Kline getKlineByNameAndDate(String name, Date date) throws KlineException;

    List<Kline> getKlineCurveByName(String name) throws KlineException;

    List<Kline> getKlineCurveByNameAndDuration(String name, Date startDate, Date endDate) throws KlineException;
}

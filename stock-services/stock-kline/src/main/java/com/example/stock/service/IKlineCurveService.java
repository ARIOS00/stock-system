package com.example.stock.service;

import com.example.stock.entity.Kline;
import com.example.stock.exception.KlineException;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface IKlineCurveService {
    Kline getLatestKlineByName(String name) throws KlineException, ParseException;
    Kline getKlineByNameAndDate(String name, Date date) throws KlineException, ParseException;

    List<Kline> getKlineCurveByName(String name) throws KlineException;

    List<Kline> getKlineCurveByNameAndDuration(String name, Date startDate, Date endDate) throws KlineException;
}

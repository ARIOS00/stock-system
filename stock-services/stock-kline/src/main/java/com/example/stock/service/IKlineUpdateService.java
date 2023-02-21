package com.example.stock.service;

import com.example.stock.entity.Kline;
import com.example.stock.exception.KlineException;

import java.util.List;

public interface IKlineUpdateService {
    public List<Kline> KlineCurveUpdate(List<Kline> klines) throws KlineException;
}

package com.example.stock.service;

import com.example.stock.entity.Kline;

import java.util.List;

public interface IAsyncService {
    public void asyncUpdateKlineCurve(List<Kline> klines);
}

package com.example.stock.service;

import com.example.stock.entity.Kline;
import com.example.stock.exception.KlineException;
import com.example.stock.exception.UserException;
import com.example.stock.vo.UserSDK;

import java.util.List;

public interface IKlineUpdateService {
    public List<Kline> KlineCurveUpdate(List<Kline> klines, UserSDK userSDK) throws KlineException, UserException;
}

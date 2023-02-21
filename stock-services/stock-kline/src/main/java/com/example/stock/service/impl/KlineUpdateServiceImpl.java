package com.example.stock.service.impl;

import com.example.stock.entity.Kline;
import com.example.stock.exception.KlineException;
import com.example.stock.service.IKlineUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class KlineUpdateServiceImpl implements IKlineUpdateService {

    @Override
    public List<Kline> KlineCurveUpdate(List<Kline> klines) throws KlineException {

        return null;
    }
}

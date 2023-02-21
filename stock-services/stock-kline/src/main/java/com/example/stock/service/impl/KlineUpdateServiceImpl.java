package com.example.stock.service.impl;

import com.example.stock.entity.Kline;
import com.example.stock.exception.KlineException;
import com.example.stock.exception.UserException;
import com.example.stock.service.IKlineUpdateService;
import com.example.stock.vo.UserSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class KlineUpdateServiceImpl implements IKlineUpdateService {

    @Override
    public List<Kline> KlineCurveUpdate(List<Kline> klines, UserSDK userSDK) throws KlineException, UserException {
        if(null == userSDK)
            throw new UserException("please login!");
        if(null == klines || CollectionUtils.isEmpty(klines))
            throw new KlineException("kline list cannot be null or empty!");
        if(klines.size() > 100)
            throw new KlineException(("maximum 100 kline points per time!"));


        return null;
    }
}

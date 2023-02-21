package com.example.stock.service.impl;

import com.example.stock.consts.KlineConst;
import com.example.stock.entity.Kline;
import com.example.stock.exception.KlineException;
import com.example.stock.exception.UserException;
import com.example.stock.service.IAsyncService;
import com.example.stock.service.IKlineUpdateService;
import com.example.stock.vo.UserSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class KlineUpdateServiceImpl implements IKlineUpdateService {

    private final IAsyncService asyncService;

    public KlineUpdateServiceImpl (IAsyncService asyncService) {
        this.asyncService = asyncService;
    }

    @Override
    public List<Kline> klineCurveUpdate(List<Kline> klines, UserSDK userSDK) throws KlineException, UserException {
        if(null == userSDK)
            throw new UserException("please login!");
        if(null == klines || CollectionUtils.isEmpty(klines))
            throw new KlineException("kline list cannot be null or empty!");
        if(klines.size() > 100)
            throw new KlineException(("maximum 100 kline points per time!"));
        List<List<Kline>> klineMat = new LinkedList<>();
        splitList(klines, klineMat);
        for(List<Kline> curve: klineMat) {
            asyncService.asyncUpdateKlineCurve(curve);
        }
        return klines;
    }

    public <T> void splitList(List<T> list, List<List<T>> res) {
        if(list.size() <= KlineConst.MAX_UPDATE_NUM){
            res.add(list);
            return;
        }
        int mid = list.size() / 2;
        splitList(list.subList(0, mid), res);
        splitList(list.subList(mid, list.size()), res);
    }
}

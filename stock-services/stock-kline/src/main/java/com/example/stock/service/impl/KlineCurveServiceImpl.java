package com.example.stock.service.impl;

import com.example.stock.dao.KlineDao;
import com.example.stock.entity.Kline;
import com.example.stock.exception.KlineException;
import com.example.stock.service.IKlineCurveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class KlineCurveServiceImpl implements IKlineCurveService {

    private final KlineDao klineDao;

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public KlineCurveServiceImpl(KlineDao klineDao, StringRedisTemplate redisTemplate) {
        this.klineDao = klineDao;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Kline getLatestKlineByName(String name) throws KlineException {
        String dateStr = redisTemplate.opsForValue().get("latest_kline_date");
//        SimpleDateFormat format = new SimpleDateFormat("")
        return null;
    }

    @Override
    public Kline getKlineByNameAndDate(String name, Date date) throws KlineException {
        return null;
    }

    @Override
    public List<Kline> getKlineCurveByName(String name) throws KlineException {
        return null;
    }

    @Override
    public List<Kline> getKlineCurveByNameAndDuration(String name, Date startDate, Date endDate) throws KlineException {
        return null;
    }
}

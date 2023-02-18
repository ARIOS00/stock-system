package com.example.stock.service.impl;

import com.example.stock.dao.KlineDao;
import com.example.stock.entity.Kline;
import com.example.stock.exception.KlineException;
import com.example.stock.service.IKlineCurveService;
import com.example.stock.util.GoogleBloomFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class KlineCurveServiceImpl implements IKlineCurveService {

    private final KlineDao klineDao;

    private final StringRedisTemplate redisTemplate;

    private final GoogleBloomFilter bloomFilter;

    @Autowired
    public KlineCurveServiceImpl(KlineDao klineDao, StringRedisTemplate redisTemplate, GoogleBloomFilter bloomFilter) {
        this.klineDao = klineDao;
        this.redisTemplate = redisTemplate;
        this.bloomFilter = bloomFilter;
    }

    @Override
    public Kline getLatestKlineByName(String name) throws KlineException, ParseException {
        // start bloom filter
        if(!bloomFilter.isExist(name))
            return null;
        // check the latest date
        String dateStr = redisTemplate.opsForValue().get("latest_kline_date");
        if(StringUtils.isEmpty(dateStr))
            throw new KlineException("latest kline date is missing in redis!");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(dateStr);
        String key = "kline:" + name + ":" + dateStr;

        // query data in redis or db
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        if(!MapUtils.isEmpty(map))
            return new Kline().getKline(map);
        Kline kline = klineDao.findKlineByNameAndKdate(name, date);
        if(null == kline)
            throw new KlineException("cannot find " + key + " in database!");
        redisTemplate.opsForHash().putAll(key, kline.getMap());
        return kline;
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

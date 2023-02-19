package com.example.stock.service.impl;

import com.example.stock.consts.KlineConst;
import com.example.stock.dao.KlineDao;
import com.example.stock.entity.Kline;
import com.example.stock.exception.KlineException;
import com.example.stock.service.IKlineCurveService;
import com.example.stock.util.GoogleBloomFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class KlineCurveServiceImpl implements IKlineCurveService {

    private final KlineDao klineDao;

    private final StringRedisTemplate redisTemplate;

    private final GoogleBloomFilter bloomFilter;

    private Date startDate;

    @Autowired
    public KlineCurveServiceImpl(KlineDao klineDao, StringRedisTemplate redisTemplate, GoogleBloomFilter bloomFilter) throws ParseException {
        this.klineDao = klineDao;
        this.redisTemplate = redisTemplate;
        this.bloomFilter = bloomFilter;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.startDate = format.parse(KlineConst.START_DATE);
    }

    @Override
    public Kline getLatestKlineByName(String name) throws KlineException, ParseException {
        // start bloom filter
        if(!bloomFilter.nameIsExist(name))
            return null;
        // check the latest date
        Date date = getLatestDateFromRedis();
        // query data in redis or db
        return queryKline(name, date);
    }

    @Override
    public Kline getKlineByNameAndDate(String name, Date date) throws KlineException, ParseException {
        if(!bloomFilter.nameIsExist(name))
            return null;
        Date latestDate = getLatestDateFromRedis();
        if(!bloomFilter.dateIsExist(date))
            return null;
        return queryKline(name, date);
    }

    @Override
    public List<Kline> getKlineCurveByName(String name) throws KlineException {
        if(!bloomFilter.nameIsExist(name))
            return null;
        return klineDao.findKlinesByName(name);
    }

    @Override
    public List<Kline> getKlineCurveByNameAndDuration(String name, Date startDate, Date endDate) throws KlineException, ParseException {
        if(!bloomFilter.nameIsExist(name))
            return null;
        if(startDate.after(endDate))
            throw new KlineException("start date should be before the end date!");
        if(startDate.before(this.startDate) || endDate.after(getLatestDateFromRedis()))
            return null;
        List<Kline> curve = new LinkedList<>();
        Calendar currentDay = Calendar.getInstance();
        Calendar endDay = Calendar.getInstance();
        currentDay.setTime(startDate);
        endDay.setTime(endDate);
        while(currentDay.before(endDay) || currentDay.equals(endDay)) {
            if(bloomFilter.dateIsExist(currentDay.getTime()))
                curve.add(queryKline(name, currentDay.getTime()));
            currentDay.add(Calendar.DAY_OF_MONTH, 1);
        }
        return curve;
    }

    private Date getLatestDateFromRedis() throws KlineException, ParseException {
        String dateStr = redisTemplate.opsForValue().get("latest_kline_date");
        if(StringUtils.isEmpty(dateStr))
            throw new KlineException("latest kline date is missing in redis!");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(dateStr);
    }

    private Kline queryKline(String name, Date date) throws KlineException, ParseException {
        String key = "kline:" + name + ":" + new SimpleDateFormat("yyyy-MM-dd").format(date);
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        if(!MapUtils.isEmpty(map))
            return new Kline().getKline(map);
        Kline kline = klineDao.findKlineByNameAndKdate(name, date);
        if(null == kline) {
            throw new KlineException("cannot find " + key + " in database!");
        }
        redisTemplate.opsForHash().putAll(key, kline.getMap());
        Integer expiration = new Random().nextInt(KlineConst.EXPIRE_SEED) + KlineConst.EXPIRE_SEED;
        redisTemplate.expire(key, expiration, TimeUnit.SECONDS);
        return kline;
    }
}

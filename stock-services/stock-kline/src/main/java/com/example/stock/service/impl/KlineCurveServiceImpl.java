package com.example.stock.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.stock.consts.KlineConst;
import com.example.stock.dao.KlineDao;
import com.example.stock.entity.Kline;
import com.example.stock.entity.KlineDefault;
import com.example.stock.exception.KlineException;
import com.example.stock.service.IKlineCurveService;
import com.example.stock.util.CSVHelper;
import com.example.stock.util.GoogleBloomFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KlineCurveServiceImpl implements IKlineCurveService {

    private final KlineDao klineDao;

    private final RedisTemplate<String, Object> redisTemplate;

    private final GoogleBloomFilter bloomFilter;

    private final StringRedisTemplate stringRedisTemplate;

    private Date startDate;

    @PostConstruct
    public void init() {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(KlineDefault.class));
    }

    @Autowired
    public KlineCurveServiceImpl(KlineDao klineDao, RedisTemplate<String, Object> redisTemplate, GoogleBloomFilter bloomFilter, StringRedisTemplate stringRedisTemplate) throws ParseException {
        this.klineDao = klineDao;
        this.redisTemplate = redisTemplate;
        this.bloomFilter = bloomFilter;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.startDate = format.parse(KlineConst.START_DATE);
        this.stringRedisTemplate = stringRedisTemplate;
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
        if(!bloomFilter.dateIsExist(date))
            return null;
        return queryKline(name, date);
    }

    @Override
    public List<Kline> getKlineCurveByName(String name) throws KlineException, ParseException {
        if(!bloomFilter.nameIsExist(name))
            return null;
        Date startDate = this.startDate;
        Date endDate = getLatestDateFromRedis();
        return getKlineCurveByNameAndDuration(name, startDate, endDate);
    }

    @Override
    public List<Kline> getKlineCurveByNameAndDuration(String name, Date startDate, Date endDate) throws KlineException, ParseException {
        if(!bloomFilter.nameIsExist(name))
            return null;
        if(startDate.after(endDate))
            throw new KlineException("start date should be before the end date!");
        if(startDate.before(this.startDate) || endDate.after(getLatestDateFromRedis()))
            return null;

        Calendar currentDay = Calendar.getInstance();
        Calendar endDay = Calendar.getInstance();
        currentDay.setTime(startDate);
        endDay.setTime(endDate);

        String baseKey = "kline:" + name + ":";
        List<String> keys = new LinkedList<>();
        List<Date> dates = new LinkedList<>();

        while(currentDay.before(endDay) || currentDay.equals(endDay)) {
            if(bloomFilter.dateIsExist(currentDay.getTime())) {
                String key = baseKey + new SimpleDateFormat("yyyy-MM-dd").format(currentDay.getTime());
                keys.add(key);
                dates.add(currentDay.getTime());
            }
            currentDay.add(Calendar.DAY_OF_MONTH, 1);
        }

        List<Object> objects = redisTemplate.opsForValue().multiGet(keys);
        if(null == objects)
            throw new KlineException("multi get null");
        int length = objects.size();
        SessionCallback<Object> sessionCallback = new SessionCallback<Object> () {
            @Override
            public Object execute(RedisOperations ops) throws DataAccessException {
                for(int i = 0; i < length; i++) {
                    if(null == objects.get(i)) {
                        Kline obj = klineDao.findKlineByNameAndKdate(name, dates.get(i));
                        objects.set(i, obj);
                        Integer expiration = new Random().nextInt(KlineConst.EXPIRE_SEED) + KlineConst.EXPIRE_SEED;
                        ops.opsForValue().set(keys.get(i), new KlineDefault(obj), Duration.ofSeconds(expiration));
                    }
                }
                return null;
            }
        };

        redisTemplate.executePipelined(sessionCallback);
        log.info("Redis Pipeline executed!");

        return objectsToKlines(objects);
    }

    @Override
    public ByteArrayInputStream getKlineCSVByName(String name) throws KlineException, ParseException {
        if(!bloomFilter.nameIsExist(name))
            return null;
        List<Kline> curve = getKlineCurveByName(name);
        return CSVHelper.tutorialsToCSV(curve);
    }

    private Date getLatestDateFromRedis() throws KlineException, ParseException {
        String dateStr = stringRedisTemplate.opsForValue().get("latest_kline_date");
        if(StringUtils.isEmpty(dateStr))
            throw new KlineException("latest kline date is missing in redis!");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(dateStr);
    }

    private Kline queryKline(String name, Date date) throws KlineException, ParseException {
        String key = "kline:" + name + ":" + new SimpleDateFormat("yyyy-MM-dd").format(date);
        KlineDefault klineDefault = (KlineDefault) redisTemplate.opsForValue().get(key);
        if(null != klineDefault) {
            log.warn("from redis");
            return new Kline(klineDefault);
        }

        Kline kline = klineDao.findKlineByNameAndKdate(name, date);
        if(null == kline) {
            log.error("cannot find " + key + " in database!");
        }
        redisTemplate.opsForValue().set(key, new KlineDefault(kline));
        Integer expiration = new Random().nextInt(KlineConst.EXPIRE_SEED) + KlineConst.EXPIRE_SEED;
        redisTemplate.expire(key, expiration, TimeUnit.SECONDS);
        return kline;
    }

    private List<Kline> objectsToKlines(List<Object> objects) {
        List<Kline> klines = objects.stream().map(object -> {
            try {
                return new Kline((KlineDefault) object);
            }catch (Exception e) {
                return (Kline) object;
            }
        }).collect(Collectors.toList());
        return klines;
    }

}

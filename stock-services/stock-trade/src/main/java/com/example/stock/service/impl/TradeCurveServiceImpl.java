package com.example.stock.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.stock.dao.TradeDao;
import com.example.stock.entity.Trade;
import com.example.stock.entity.TradeDefault;
import com.example.stock.exception.TradeException;
import com.example.stock.service.ITradeCurveService;
import com.example.stock.util.GoogleBloomFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TradeCurveServiceImpl implements ITradeCurveService {

    private final GoogleBloomFilter bloomFilter;

    private final TradeDao tradeDao;

    private final RedisTemplate redisTemplate;

    @Autowired
    public TradeCurveServiceImpl(GoogleBloomFilter bloomFilter, TradeDao tradeDao, RedisTemplate redisTemplate) {
        this.bloomFilter = bloomFilter;
        this.tradeDao = tradeDao;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Trade> getTradeCurveByName(String name) throws TradeException, ParseException {
        if(!bloomFilter.nameIsExist(name)){
            log.warn("no such name in bloom filter!");
            return null;
        }

        Calendar currentDay = Calendar.getInstance();
        Calendar endDay = Calendar.getInstance();

        currentDay.setTime(getTradeStartDate());
        endDay.setTime(new Date());

        Calendar limit = Calendar.getInstance();
        limit.setTime(getTradeEndDate());
        if(endDay.after(limit))
            endDay.setTime(getTradeEndDate());

        List<String> keys = new LinkedList<>();
        List<Date> dates = new LinkedList<>();
        String baseKey = "trade:" + name + ":";

        while(currentDay.before(endDay) || currentDay.equals(endDay)) {
            String key = baseKey + new SimpleDateFormat("yyyy-MM-dd HH:mm:").format(currentDay.getTime());
            key += "00";
            keys.add(key);
            dates.add(currentDay.getTime());
            currentDay.add(Calendar.MINUTE, 1);
        }

        List<Object> objects = redisTemplate.opsForValue().multiGet(keys);
        if(null == objects)
            throw new TradeException("multi get null");
        int length = objects.size();

        SessionCallback<Object> sessionCallback = new SessionCallback<Object> () {
            @Override
            public Object execute(RedisOperations ops) throws DataAccessException {
                for(int i = 0; i < length; i++) {
                    if(null != objects.get(i))
                        continue;
                    Trade obj = tradeDao.findTradeByNameAndFreshTime(name, dates.get(i));
                    if(null == obj) {
                        log.warn("trade [{}, {}] is empty in db! the relative key in redis would be set as 0!", name, dates.get(i));
                        ops.opsForValue().set(keys.get(i), new TradeDefault(), Duration.ofDays(1));
                        continue;
                    }
                    objects.set(i, obj);
                    ops.opsForValue().set(keys.get(i), new TradeDefault(obj), Duration.ofDays(1));
                }
                return null;
            }
        };

        redisTemplate.executePipelined(sessionCallback);
        log.info("Redis Pipeline executed!");


        return objectsToTrades(objects);
    }

    private Date getTradeStartDate() throws ParseException {
        Date startDate = new Date();
        String start = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
        start += " 09:30:00";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(start);
    }

    private Date getTradeEndDate() throws ParseException {
        Date endDate = new Date();
        String end = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
        end += " 16:00:00";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end);
    }

    private List<Trade> objectsToTrades(List<Object> objects) {


        List<Trade> trades = objects.stream().map(object -> {
            try {
                return new Trade((TradeDefault) object);
            }catch (Exception e) {
                return (Trade) object;
            }
        }).collect(Collectors.toList());

        trades = trades.stream()
                .filter(trade -> (trade != null && trade.getName() != null))
                .collect(Collectors.toList());
        return trades;
    }
}

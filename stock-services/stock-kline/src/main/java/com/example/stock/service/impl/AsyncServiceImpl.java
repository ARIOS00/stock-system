package com.example.stock.service.impl;

import com.example.stock.consts.KlineConst;
import com.example.stock.dao.KlineDao;
import com.example.stock.entity.Kline;
import com.example.stock.entity.KlineDefault;
import com.example.stock.service.IAsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class AsyncServiceImpl implements IAsyncService {
    private final KlineDao klineDao;

    private final RedisTemplate redisTemplate;

    private SimpleDateFormat format;

    public AsyncServiceImpl(KlineDao klineDao, RedisTemplate redisTemplate) {
        this.klineDao = klineDao;
        this.redisTemplate = redisTemplate;
        this.format = new SimpleDateFormat("yyyy-MM-dd");
    }
    @Async("getAsyncExecutor")
    @Override
    public void asyncUpdateKlineCurve(List<Kline> klines) {
        log.info("async task started, thread name: {}", Thread.currentThread().getName());
        SessionCallback<Object> sessionCallback = new SessionCallback<Object> () {
            @Override
            public Object execute(RedisOperations ops) throws DataAccessException {
                for(Kline kline : klines) {
                    if(kline == null)
                        continue;
                    String key = "kline:" + kline.getName() + format.format(kline.getKdate());
                    Integer expiration = new Random().nextInt(KlineConst.EXPIRE_SEED) + KlineConst.EXPIRE_SEED;
                    ops.opsForValue().set(key, new KlineDefault(kline), Duration.ofSeconds(expiration));
                    klineDao.save(kline);
                }
                return null;
            }
        };

        redisTemplate.executePipelined(sessionCallback);
        log.info("Redis Pipeline executed!");
    }
}

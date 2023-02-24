package com.example.stock.util;

import com.example.stock.dao.TradeDao;
import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@SuppressWarnings("all")
public class GoogleBloomFilter {
    private BloomFilter nameBloomFilter;

    @Autowired
    private TradeDao tradeDao;

    @PostConstruct
    public void initBloomFilter() {
        Set<String> names = tradeDao.findAllNames();
        Set<Date> dateSet = tradeDao.findAllFreshTimes();
        nameBloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), names.size(), 0.0002);
        for(String name : names) {
            nameBloomFilter.put(name);
        }
    }

    public boolean nameIsExist(String str) {
        return nameBloomFilter.mightContain(str);
    }


    public void updateNameFilter(String name) {
        nameBloomFilter.put(name);
    }
}
package com.example.stock.util;

import com.alibaba.fastjson.JSON;
import com.example.stock.dao.KlineDao;
import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@SuppressWarnings("all")
public class GoogleBloomFilter {
    private BloomFilter nameBloomFilter;

    private BloomFilter dateBloomFilter;

    @Autowired
    private KlineDao klineDao;

    @PostConstruct
    public void initBloomFilter() {
        Set<String> names = klineDao.findAllNames();
        Set<Date> dateSet = klineDao.findAllDates();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Set<String> dates = dateSet.stream()
                .map(date -> format.format(date))
                .collect(Collectors.toSet());
        nameBloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), names.size(), 0.0002);
        dateBloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), dates.size(), 0.0002);
        for(String name : names) {
            nameBloomFilter.put(name);
        }
        for(String date : dates) {
            dateBloomFilter.put(date);
        }
    }

    public boolean nameIsExist(String str) {
        return nameBloomFilter.mightContain(str);
    }

    public boolean dateIsExist(Date date) {
        return dateBloomFilter.mightContain(new SimpleDateFormat("yyyy-MM-dd").format(date));
    }

    public void updateNameFilter(List<String> names) {
        for(String name : names) {
            nameBloomFilter.put(name);
        }
    }

    public void updateDateFilter(List<Date> dates) {
        for(Date date : dates) {
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
            nameBloomFilter.put(dateStr);
        }
    }
}

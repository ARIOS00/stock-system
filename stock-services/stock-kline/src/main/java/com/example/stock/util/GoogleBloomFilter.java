package com.example.stock.util;

import com.example.stock.dao.KlineDao;
import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
@SuppressWarnings("all")
public class GoogleBloomFilter {
    private BloomFilter bloomFilter;

    @Autowired
    private KlineDao klineDao;

    @PostConstruct
    public void initBloomFilter() {
        Set<String> names = klineDao.findAllNames();
        bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), names.size(), 0.001);
        for(String name : names) {
            bloomFilter.put(name);
        }
    }

    public boolean isExist(String str) {
        return bloomFilter.mightContain(str);
    }
}

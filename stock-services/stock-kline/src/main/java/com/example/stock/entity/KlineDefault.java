package com.example.stock.entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize()
public class KlineDefault extends Kline {
    // Constructor to copy fields from the parent class
    public KlineDefault(Kline kline) {
        this.setName(kline.getName());
        this.setKdate(kline.getKdate());
        this.setClose(kline.getClose());
        this.setVolume(kline.getVolume());
        this.setOpen(kline.getOpen());
        this.setHigh(kline.getHigh());
        this.setLow(kline.getLow());
    }

    // Constructor to create a new instance with default values
    public KlineDefault() {
        super();
    }

}
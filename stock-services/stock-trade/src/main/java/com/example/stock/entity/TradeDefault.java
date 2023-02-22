package com.example.stock.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize()
public class TradeDefault extends Trade{
    // Constructor to copy fields from the parent class
    public TradeDefault(Trade trade) {
        this.setName(trade.getName());
        this.setFreshTime(trade.getFreshTime());
        this.setPrice(trade.getPrice());
        this.setDiff(trade.getDiff());
        this.setRate(trade.getRate());
    }

    // Constructor to create a new instance with default values
    public TradeDefault() {
        super();
    }
}

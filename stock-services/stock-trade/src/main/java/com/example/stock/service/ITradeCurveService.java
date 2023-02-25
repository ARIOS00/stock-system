package com.example.stock.service;

import com.example.stock.entity.Trade;
import com.example.stock.exception.TradeException;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface ITradeCurveService {

    List<Trade> getTradeCurveByName(String Name) throws TradeException, ParseException;

    Trade getTradeByNameAndTime(String name, Date freshTime) throws TradeException, ParseException;
}

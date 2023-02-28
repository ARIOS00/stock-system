package com.example.stock.controller;

import com.example.stock.entity.Trade;
import com.example.stock.exception.TradeException;
import com.example.stock.service.ITradeCurveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
public class TradeController {
    private final ITradeCurveService tradeCurveService;

    public TradeController(ITradeCurveService tradeCurveService) {
        this.tradeCurveService = tradeCurveService;
    }

    // localhost:7002/stock-trade/trade
    // localhost:9000/stock/stock-trade/trade
    @GetMapping("/trade")
    public Trade getTradeByNameAndTime(@RequestParam String name, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date freshTime) throws TradeException, ParseException {
        return tradeCurveService.getTradeByNameAndTime(name, freshTime);
    }

    @GetMapping("/trade_curve")
    public List<Trade> getTradeCurveByName(@RequestParam String name) throws TradeException, ParseException {
        return tradeCurveService.getTradeCurveByName(name);
    }
}

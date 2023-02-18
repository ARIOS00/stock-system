package com.example.stock.controller;

import com.example.stock.entity.Kline;
import com.example.stock.service.IKlineCurveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class KlineController {
    private final IKlineCurveService klineCurveService;


    @Autowired
    public KlineController(IKlineCurveService klineCurveService) {
        this.klineCurveService = klineCurveService;
    }

    // localhost:7002/stock-kline/kline
    @GetMapping("/kline")
    public Kline getLatestKlineByName(@RequestParam String name) throws Exception {
        return klineCurveService.getLatestKlineByName(name);
    }
}

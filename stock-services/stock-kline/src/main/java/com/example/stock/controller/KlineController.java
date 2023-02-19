package com.example.stock.controller;

import com.example.stock.entity.Kline;
import com.example.stock.exception.KlineException;
import com.example.stock.service.IKlineCurveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
public class KlineController {
    private final IKlineCurveService klineCurveService;


    @Autowired
    public KlineController(IKlineCurveService klineCurveService) {
        this.klineCurveService = klineCurveService;
    }

    // localhost:7002/stock-kline/kline
    // localhost:9000/stock/stock-kline/kline
    @GetMapping("/kline_latest")
    public Kline getLatestKlineByName(@RequestParam String name) throws Exception {
        return klineCurveService.getLatestKlineByName(name);
    }

    @GetMapping("/kline")
    public Kline getLatestKlineByNameAndDate(@RequestParam String name, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) throws Exception {
        return klineCurveService.getKlineByNameAndDate(name, date);
    }

    @GetMapping("/kline_intact_curve")
    public List<Kline> getKlineCurveByName(@RequestParam String name) throws KlineException {
        return klineCurveService.getKlineCurveByName(name);
    }

    @GetMapping("/kline_curve")
    public List<Kline> getKlineCurveByNameAndDuration(@RequestParam String name, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws Exception {
        return  klineCurveService.getKlineCurveByNameAndDuration(name, startDate, endDate);
    }
}

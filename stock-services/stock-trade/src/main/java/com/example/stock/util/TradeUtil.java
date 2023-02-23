package com.example.stock.util;

import com.example.stock.entity.Trade;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TradeUtil {
    public static Trade StringToTrade(String str) throws ParseException {
        String[] arrStr = str.split(",");
        Trade trade = new Trade();
        trade.setName(arrStr[0]);
        trade.setPrice(Double.parseDouble(arrStr[1]));
        trade.setDiff(Double.parseDouble(arrStr[2]));
        trade.setRate(Double.parseDouble(arrStr[3]));

        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        trade.setFreshTime(format.parse(arrStr[4]));

        return trade;
    }
}

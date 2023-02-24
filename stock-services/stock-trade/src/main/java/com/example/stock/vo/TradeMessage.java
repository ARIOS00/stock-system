package com.example.stock.vo;

import com.example.stock.entity.Trade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeMessage {
    private Trade trade;

    private Long offset;
}

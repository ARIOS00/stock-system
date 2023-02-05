package com.example.stock.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ResBean {
    SUCCESS("success", 200),
    FAIL("fail", 500);

    private String description;
    private Integer code;
}

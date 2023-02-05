package com.example.stock.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> {
    String msg;
    Integer code;
    T content;

    public CommonResponse (Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
}

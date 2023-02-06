package com.example.stock.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSDK {
    private Long id;

    private String nickname;

    private Date registerDate;

    private Date lastLoginDate;

    private Integer loginCount;
}

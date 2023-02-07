package com.example.stock.vo;

import com.example.stock.serialization.UserSDKSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(using = UserSDKSerialize.class)
public class UserSDK implements Serializable {
    private Long id;

    private String nickname;

    private Date registerDate;

    private Date lastLoginDate;

    private Integer loginCount;

    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        map.put("id", this.getId().toString());
        map.put("nickname", this.getNickname());
        map.put("registerDate", this.getRegisterDate().toString());
        map.put("lastLoginDate", this.getLastLoginDate().toString());
        map.put("loginCount", this.getLoginCount().toString());
        return map;
    }

    public UserSDK getUserSDK(Map<Object, Object> objMap) throws ParseException {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<Object, Object> entry : objMap.entrySet()) {
            map.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }

        this.setId(Long.parseLong(map.get("id")));
        this.setNickname(map.get("nickname"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.setRegisterDate(formatter.parse(map.get("registerDate")));
        this.setLastLoginDate(formatter.parse(map.get("lastLoginDate")));

        this.setLoginCount(Integer.parseInt(map.get("loginCount")));
        return this;
    }
}

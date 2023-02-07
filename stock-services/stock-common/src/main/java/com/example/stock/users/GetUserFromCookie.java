package com.example.stock.users;

import com.alibaba.fastjson.JSON;
import com.example.stock.exception.UserException;
import com.example.stock.vo.UserSDK;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class GetUserFromCookie {
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public GetUserFromCookie(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public UserSDK get(String cookie) throws Exception {
        if(StringUtils.isEmpty(cookie))
            throw new UserException("cookie is empty!");
        log.info("try to get user info based on cookie: user:{}", cookie);
        Map<Object, Object> map =  redisTemplate.opsForHash().entries("user:" + cookie);
        System.out.println("jjjj: " + map.get("registerDate"));
        UserSDK userSDK = new UserSDK();
        userSDK = userSDK.getUserSDK(map);
        System.out.println("oooooo:" + userSDK.getId());
        System.out.println("oooooo:" + userSDK.getNickname());
        System.out.println("oooooo:" + userSDK.getLastLoginDate());
        System.out.println("oooooo:" + userSDK.getRegisterDate());
        System.out.println("oooooo:" + userSDK.getLoginCount());
        if(null == userSDK)
            throw new UserException("cannot find user based on cookie: user:" + cookie);
        log.info("current user is: {}", userSDK);
        return userSDK;
    }
}

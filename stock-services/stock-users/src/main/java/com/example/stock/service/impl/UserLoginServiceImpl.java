package com.example.stock.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.stock.dao.UserDao;
import com.example.stock.entity.User;
import com.example.stock.exception.StockException;
import com.example.stock.service.IUserLoginService;
import com.example.stock.util.CookieUtil;
import com.example.stock.util.MD5Util;
import com.example.stock.util.UUIDUtil;
import com.example.stock.util.UserSDKMapper;
import com.example.stock.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserLoginServiceImpl implements IUserLoginService {
    private final UserDao userDao;

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public UserLoginServiceImpl(UserDao userDao, StringRedisTemplate redisTemplate) {
        this.userDao = userDao;
        this.redisTemplate = redisTemplate;
    }
    @Override
    public UserSDK login(String cookie, UserLoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) throws StockException {

        if(null == loginRequest.getId() || StringUtils.isEmpty(loginRequest.getPassword())) {
            throw new StockException("fields can be empty!");
        }

        User user = userDao.findById(loginRequest.getId());

        if(Objects.isNull(user)) {
            throw new StockException("phone number not exist!");
        } else if(!StringUtils.equals(user.getPassword(), MD5Util.inputPassToDBPass(loginRequest.getPassword(), user.getSalt()))) {
            throw new StockException("password incorrect!");
        }
        user.setLoginCount(user.getLoginCount() + 1);
        Date date = new java.util.Date();
        user.setLastLoginDate(date);
        userDao.save(user);
        UserSDK userSDK = UserSDKMapper.userToUserSDK(user);
        //generate cookie
        String ticket = UUIDUtil.uuid();

        Map<String, String> map = userSDK.getMap();
        redisTemplate.opsForHash().putAll("user:" + ticket, map);
        CookieUtil.setCookie(request, response, "userTicket", ticket);

        return userSDK;
    }

    @Override
    public UserSDK getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.isEmpty(userTicket)) return null;
        UserSDK userSDK = null;
        if(null != userSDK){
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return userSDK;
    }

}

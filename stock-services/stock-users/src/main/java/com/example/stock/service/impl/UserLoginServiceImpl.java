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
import com.example.stock.vo.CommonResponse;
import com.example.stock.vo.ResBean;
import com.example.stock.vo.UserLoginRequest;
import com.example.stock.vo.UserSDK;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Slf4j
@Service
public class UserLoginServiceImpl implements IUserLoginService {
    private final UserDao userDao;
    private final RedisTemplate redisTemplate;

    @Autowired
    public UserLoginServiceImpl(UserDao userDao, RedisTemplate redisTemplate) {
        this.userDao = userDao;
        this.redisTemplate = redisTemplate;
    }
    @Override
    public CommonResponse<UserSDK> login(UserLoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) throws StockException {
        CommonResponse<UserSDK> commonResponse = new CommonResponse<>();
        commonResponse.setCode(ResBean.FAIL.getCode());

        if(null == loginRequest.getId() || StringUtils.isEmpty(loginRequest.getPassword())) {
            commonResponse.setMsg("fields can be empty!");
            return commonResponse;
        }

        User user = userDao.findById(loginRequest.getId());
        if(Objects.isNull(user)) {
            commonResponse.setMsg("phone number not exist!");
        } else if(!StringUtils.equals(user.getPassword(), MD5Util.inputPassToDBPass(loginRequest.getPassword(), user.getSalt()))) {
            commonResponse.setMsg("password incorrect!");
        } else {
            commonResponse.setCode(ResBean.SUCCESS.getCode());
            commonResponse.setMsg("login success!");
            commonResponse.setContent(UserSDKMapper.userToUserSDK(user));

            user.setLoginCount(user.getLoginCount() + 1);
            java.util.Date date = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            user.setLastLoginDate(sqlDate);
            userDao.save(user);

            //generate cookie
            String ticket = UUIDUtil.uuid();
            redisTemplate.opsForValue().set("user:" + ticket, JSON.toJSONString(UserSDKMapper.userToUserSDK(user)));
            CookieUtil.setCookie(request, response, "userTicket", ticket);
        }
       return commonResponse;
    }

    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.isEmpty(userTicket)) return null;
        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
        if(null != user){
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }
}

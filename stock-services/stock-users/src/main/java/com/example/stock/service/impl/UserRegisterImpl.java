package com.example.stock.service.impl;

import com.example.stock.dao.UserDao;
import com.example.stock.entity.User;
import com.example.stock.exception.StockException;
import com.example.stock.service.IUserRegister;
import com.example.stock.util.MD5Util;
import com.example.stock.util.RandomStringGenerator;
import com.example.stock.vo.UserRegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class UserRegisterImpl implements IUserRegister {
    private final UserDao userDao;

    @Autowired
    public UserRegisterImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User register(UserRegisterRequest request) throws StockException {
        if(request.getId() == null || StringUtils.isEmpty(request.getNickname()) || StringUtils.isEmpty(request.getPassword()))
            throw new StockException("register fields cannot be empty!");
        if(userDao.findById(request.getId()) != null)
            throw new StockException("phone number has been already registered!");
        if(request.getPassword().length() < 6)
            throw new StockException("the length of password should not less than 6!");

        User user = requestToUser(request);
        userDao.save(user);
        return user;
    }

    private User requestToUser(UserRegisterRequest request) {
        User user = new User();
        user.setId(request.getId());
        user.setNickname(request.getNickname());

        user.setSalt(RandomStringGenerator.get(MD5Util.SALT_LENGTH));
        user.setPassword(MD5Util.inputPassToDBPass(request.getPassword(), user.getSalt()));

        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        user.setRegisterDate(sqlDate);
        user.setLastLoginDate(sqlDate);
        user.setLoginCount(0);

        return user;
    }
}

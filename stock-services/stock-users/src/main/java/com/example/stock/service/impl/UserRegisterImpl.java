package com.example.stock.service.impl;

import com.example.stock.dao.UserDao;
import com.example.stock.entity.User;
import com.example.stock.service.IUserRegister;
import com.example.stock.util.MD5Util;
import com.example.stock.util.RandomStringGenerator;
import com.example.stock.vo.UserRegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserRegisterImpl implements IUserRegister {
    private UserDao userDao;

    @Autowired
    public UserRegisterImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User register(UserRegisterRequest request) {
        return null;
    }

    private User requestToUser(UserRegisterRequest request) {
        User user = new User();
        user.setId(request.getId());
        user.setNickname(request.getNickname());
        user.setPassword(request.getPassword());

        user.setSalt(RandomStringGenerator.get(MD5Util.SALT_LENGTH));
        return user;
    }

}

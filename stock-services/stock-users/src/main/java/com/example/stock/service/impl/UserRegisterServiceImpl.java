package com.example.stock.service.impl;

import com.example.stock.dao.UserDao;
import com.example.stock.entity.User;
import com.example.stock.service.IUserRegisterService;
import com.example.stock.util.MD5Util;
import com.example.stock.util.RandomStringGenerator;
import com.example.stock.vo.CommonResponse;
import com.example.stock.vo.ResBean;
import com.example.stock.vo.UserRegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class UserRegisterServiceImpl implements IUserRegisterService {
    private final UserDao userDao;

    @Autowired
    public UserRegisterServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public CommonResponse<User> register(UserRegisterRequest request) {
        CommonResponse<User> response = new CommonResponse<>();
        response.setCode(ResBean.FAIL.getCode());
        if(request.getId() == null || StringUtils.isEmpty(request.getNickname()) || StringUtils.isEmpty(request.getPassword()))
            response.setMsg("register fields cannot be empty!");
        else if(request.getPassword().length() < 6)
            response.setMsg("the length of password should not less than 6!");
        else if(userDao.findById(request.getId()) != null)
            response.setMsg("phone number has been already registered!");
        else {
            response.setCode(ResBean.SUCCESS.getCode());
            response.setMsg("register success!");

            User user = requestToUser(request);
            userDao.save(user);
            response.setContent(user);
        }

        return response;
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

package com.example.stock.controller;

import com.alibaba.fastjson.JSON;
import com.example.stock.exception.StockException;
import com.example.stock.service.IUserLoginService;
import com.example.stock.service.IUserRegisterService;
import com.example.stock.vo.CommonResponse;
import com.example.stock.vo.UserLoginRequest;
import com.example.stock.vo.UserRegisterRequest;
import com.example.stock.vo.UserSDK;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class UserController {
    private final IUserRegisterService userRegisterService;
    private final IUserLoginService userLoginService;

    @Autowired
    public UserController(IUserRegisterService userRegisterService, IUserLoginService userLoginService) {
        this.userRegisterService = userRegisterService;
        this.userLoginService = userLoginService;
    }

    //localhost:7001/stock-users/register
    @PostMapping("/register")
    public CommonResponse<UserSDK> register(@RequestBody UserRegisterRequest request) throws StockException {
        return userRegisterService.register(request);
    }

    //localhost:7001/stock-users/login
    @PostMapping("/login")
    public CommonResponse<UserSDK> login(@RequestBody UserLoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) throws StockException {
        return userLoginService.login(loginRequest, request, response);
    }

    @GetMapping("/get")
    public String get(@CookieValue("userTicket") String cookie) {
        RequestContext context = RequestContext.getCurrentContext();
        System.out.println(cookie);
        return JSON.toJSONString(cookie);
    }
}

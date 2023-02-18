package com.example.stock.controller;

import com.example.stock.exception.StockException;
import com.example.stock.service.IUserLoginService;
import com.example.stock.service.IUserRegisterService;
import com.example.stock.users.GetUserFromCookie;
import com.example.stock.vo.UserLoginRequest;
import com.example.stock.vo.UserRegisterRequest;
import com.example.stock.vo.UserSDK;
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
    private final GetUserFromCookie getUserFromCookie;

    @Autowired
    public UserController(IUserRegisterService userRegisterService, IUserLoginService userLoginService, GetUserFromCookie getUserFromCookie) {
        this.userRegisterService = userRegisterService;
        this.userLoginService = userLoginService;
        this.getUserFromCookie = getUserFromCookie;
    }

    //localhost:7001/stock-users/register
    @PostMapping("/register")
    public UserSDK register(@RequestBody UserRegisterRequest request) throws StockException {
        return userRegisterService.register(request);
    }

    //localhost:7001/stock-users/login
    @PostMapping("/login")
    public UserSDK login(@CookieValue("userTicket") String cookie, @RequestBody UserLoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) throws StockException {
        return userLoginService.login(cookie, loginRequest, request, response);
    }

    @GetMapping("/get")
    public UserSDK get(@CookieValue("userTicket") String cookie) throws Exception {
        UserSDK userSDK = getUserFromCookie.get(cookie);
        return userSDK;
    }

}

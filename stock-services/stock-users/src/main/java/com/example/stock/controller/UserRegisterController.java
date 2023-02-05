package com.example.stock.controller;

import com.example.stock.entity.User;
import com.example.stock.exception.StockException;
import com.example.stock.service.IUserRegisterService;
import com.example.stock.vo.CommonResponse;
import com.example.stock.vo.UserRegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserRegisterController {
    private IUserRegisterService userRegisterService;

    @Autowired
    public UserRegisterController(IUserRegisterService userRegisterService) {
        this.userRegisterService = userRegisterService;
    }

    //localhost:7001/stock-users/register
    @PostMapping("/register")
    public CommonResponse<User> register(@RequestBody UserRegisterRequest request) throws StockException {
        return userRegisterService.register(request);
    }

}

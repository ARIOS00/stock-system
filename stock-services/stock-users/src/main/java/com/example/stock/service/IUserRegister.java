package com.example.stock.service;

import com.example.stock.entity.User;
import com.example.stock.vo.UserRegisterRequest;

public interface IUserRegister {
    User register(UserRegisterRequest request);
}

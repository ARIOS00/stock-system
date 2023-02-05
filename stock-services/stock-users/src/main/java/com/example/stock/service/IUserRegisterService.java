package com.example.stock.service;

import com.example.stock.entity.User;
import com.example.stock.exception.StockException;
import com.example.stock.vo.CommonResponse;
import com.example.stock.vo.UserRegisterRequest;

public interface IUserRegisterService {
    CommonResponse<User> register(UserRegisterRequest request) throws StockException;
}

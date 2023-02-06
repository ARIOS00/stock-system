package com.example.stock.service;

import com.example.stock.exception.StockException;
import com.example.stock.vo.CommonResponse;
import com.example.stock.vo.UserRegisterRequest;
import com.example.stock.vo.UserSDK;

public interface IUserRegisterService {
    CommonResponse<UserSDK> register(UserRegisterRequest request) throws StockException;
}

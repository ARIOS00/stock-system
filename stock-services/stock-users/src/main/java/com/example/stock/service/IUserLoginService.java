package com.example.stock.service;

import com.example.stock.entity.User;
import com.example.stock.exception.StockException;
import com.example.stock.vo.CommonResponse;
import com.example.stock.vo.UserLoginRequest;
import com.example.stock.vo.UserSDK;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IUserLoginService {
    UserSDK login(UserLoginRequest LoginRequest, HttpServletRequest request, HttpServletResponse response) throws StockException;

    UserSDK getUserByCookie(String userTicket,HttpServletRequest request, HttpServletResponse response);

}

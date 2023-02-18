package com.example.stock.service;

import com.example.stock.dao.UserDao;
import com.example.stock.entity.User;
import com.example.stock.exception.StockException;
import com.example.stock.users.GetUserFromCookie;
import com.example.stock.vo.UserRegisterRequest;
import com.example.stock.vo.UserSDK;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserRegisterTest {
    @Autowired
    IUserRegisterService userRegister;

    @Autowired
    UserDao userDao;

    @Autowired
    GetUserFromCookie getUserFromCookie;

    @Test
    public void testRegister() throws Exception {
//        userRegister.register(createRequest());
        UserSDK userSDK = getUserFromCookie.get("9c2e586c89d54b89891b2f5212ebc71e");
        System.out.println(userSDK.getRegisterDate());
    }

    private UserRegisterRequest createRequest() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setId(54321L);
        request.setNickname("Dodo");
        request.setPassword("123456");

        return request;
    }
}

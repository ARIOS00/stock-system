package com.example.stock.service;

import com.example.stock.exception.StockException;
import com.example.stock.vo.UserRegisterRequest;
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
    @Test
    public void testRegister() throws StockException {
        userRegister.register(createRequest());
    }

    private UserRegisterRequest createRequest() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setId(54321L);
        request.setNickname("Dodo");
        request.setPassword("123456");

        return request;
    }
}

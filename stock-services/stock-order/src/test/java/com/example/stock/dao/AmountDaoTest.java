package com.example.stock.dao;

import com.example.stock.entity.Amount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AmountDaoTest {
    @Autowired
    private AmountDao amountDao;

    @Test
    public void testAmountDao() {
        Amount amount = new Amount();
        amount.setName("AMZN");
        amount.setAmount(200);
        amountDao.save(amount);
        System.out.println(amountDao.findAmountByName("AMZN"));
    }
}

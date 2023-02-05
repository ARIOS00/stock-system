package com.example.stock.dao;

import com.example.stock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {
    User findById(Long id);
}
package com.binlicoder.houtai.service.impl;

import com.binlicoder.houtai.entity.User;
import com.binlicoder.houtai.mapper.UserMapper;
import com.binlicoder.houtai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public User findById(Integer id) {
        return userMapper.findById(id);
    }

    @Override
    public User create(User user) {
        userMapper.insert(user);
        return user;
    }

    @Override
    public User update(User user) {
        userMapper.update(user);
        return user;
    }

    @Override
    public void delete(Integer id) {
        userMapper.delete(id);
    }
}

package com.binlicoder.houtai.service;

import com.binlicoder.houtai.entity.User;

public interface UserService {
    User findByUsername(String username);
    User findById(Integer id);
    User create(User user);
    User update(User user);
    void delete(Integer id);
}

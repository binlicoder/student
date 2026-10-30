package com.binlicoder.houtai.mapper;

import com.binlicoder.houtai.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findByUsername(@Param("username") String username);
    User findById(@Param("id") Integer id);
    int insert(User user);
    int update(User user);
    int delete(@Param("id") Integer id);
}

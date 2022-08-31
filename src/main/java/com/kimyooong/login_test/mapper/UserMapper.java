package com.kimyooong.login_test.mapper;

import com.kimyooong.login_test.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User selectUserByPn(String phoneNumber);
    User selectUserById(Long userId);
    void insertUser(User user);
}

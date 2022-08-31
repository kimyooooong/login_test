package com.kimyooong.login_test.service;

import com.kimyooong.login_test.domain.User;
import com.kimyooong.login_test.exception.ServiceException;
import com.kimyooong.login_test.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserMapper userMapper;

    private PasswordEncoder passwordEncoder;

    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    public void join(Map<String,String> user){


        User insertUser = new User();
        insertUser.setPassword(passwordEncoder.encode(user.get("password")));
        insertUser.setEmail(user.get("email"));
        insertUser.setNickName(user.get("nick_name"));
        insertUser.setName(user.get("name"));
        insertUser.setPhoneNumber(user.get("phone_number"));

        log.info("user : {}" , insertUser);

        userMapper.insertUser(insertUser);
    }

    public User getUserByPn(String phoneNumber){
        return userMapper.selectUserByPn(phoneNumber);
    }

    public User getLogin(String phoneNumber , String password){

        //유저 확인.
        User selectUser = getUserByPn(phoneNumber);


        log.info("selectUser : {}" , selectUser);

        if(selectUser ==null){
            throw new ServiceException("존재하지 않는 전화번호 혹은 패스워드가 다릅니다.");
        }

        //패스워드 확인.
        if(!passwordEncoder.matches( password , selectUser.getPassword() ) ){
            throw new ServiceException("존재하지 않는 전화번호 혹은 패스워드가 다릅니다.");
        }

        return selectUser;
    }


    public User getUserById(HttpServletRequest request , Long userId){
        return userMapper.selectUserById(userId);
    }
}

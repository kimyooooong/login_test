package com.kimyooong.login_test.mapper;

import com.kimyooong.login_test.domain.ResetPassword;
import com.kimyooong.login_test.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    /* 로그인 관련 */
    User selectUserByPn(String phoneNumber);
    User selectUserById(Long userId);
    void insertUser(User user);

    /* 리셋 패스워드 관련 */
    void updateUserForPassword(User user);
    void insertResetPassword(ResetPassword resetPassword);
    ResetPassword selectResetPasswordByPhoneNumber(String phoneNumber);
    ResetPassword selectResetPasswordByRpId(Long rpId);
    void updateResetPasswordForConfirm(Long rpId);
}

package com.kimyooong.login_test.service;

import com.kimyooong.login_test.component.AES256;
import com.kimyooong.login_test.domain.ResetPassword;
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

    private final AES256 aes256;


    private PasswordEncoder passwordEncoder;
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 패스워드 - 단방향 암호화 , 그 외 개인정보 양방향 암호화.
     * @param user
     * @throws Exception
     */
    public void join(Map<String,String> user) throws Exception {

        User insertUser = new User();
        insertUser.setPassword(passwordEncoder.encode(user.get("password")));
        insertUser.setEmail(aes256.encrypt(user.get("email")));
        insertUser.setNickName(aes256.encrypt(user.get("nick_name")));
        insertUser.setName(aes256.encrypt(user.get("name")));
        insertUser.setPhoneNumber(aes256.encrypt(user.get("phone_number")));

        log.info("user : {}" , insertUser);

        userMapper.insertUser(insertUser);
    }

    /**
     *  유저 데이터 복호화
     * @param phoneNumber
     * @return
     */
    public User getUserByPn(String phoneNumber){

        User enUser = null;

        try {
            String enPhoneNumber = aes256.encrypt(phoneNumber);

            enUser = userMapper.selectUserByPn(enPhoneNumber);
            enUser.setPhoneNumber(aes256.decrypt(enUser.getPhoneNumber()));
            enUser.setEmail(aes256.decrypt(enUser.getEmail()));
            enUser.setName(aes256.decrypt(enUser.getName()));
            enUser.setNickName(aes256.decrypt(enUser.getNickName()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return enUser;
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


    public void updateUserPassword(User user){

        User selectUser = getUserByPn(user.getPhoneNumber());

        if(selectUser == null){
            throw new ServiceException("존재하지 않는 전화번호 입니다.");
        }

        userMapper.updateUserForPassword(user);
    }
    public void confirmResetPassword(Long rpId){

        ResetPassword resetPassword = userMapper.selectResetPasswordByRpId(rpId);

        if(resetPassword ==null){
            throw new ServiceException("이미 패스워드가 리셋 되었습니다.");
        }
        userMapper.updateResetPasswordForConfirm(rpId);
    }

    /**
     *
     * @param check - true 인경우 회원가입 시 사용 , false 인 경우 비밀번호 재설정 시 사용.
     * @param phoneNumber - 폰 넘버 체크. ( 폰 번호 암호화 )
     */
    public Integer certPhoneNumber(Boolean check , String phoneNumber) throws Exception {

        //회원 정보 확인.
        User selectUser = getUserByPn(phoneNumber);

        log.info("selectUser : {}" , selectUser);

        // 회원가입이고 폰번호로 가입된 번호가 있을때 오류.
        if(check) {
            if (selectUser != null) {
                throw new ServiceException("이미 가입된 전화 번호 입니다.");
            }
        } else {
        // 비밀 번호 재설정 이고 가입된 번호가 없을 때 오류
            if(selectUser == null) {
                throw new ServiceException("전화 번호가 존재 하지 않습니다.");
            }
        }

        int randomNumber = (int)(Math.random() * 1000000) + 100000;

        log.info("check : {}" , check);
        log.info("phoneNumber : {}" , phoneNumber);

        userMapper.insertResetPassword(ResetPassword.builder()
                .phoneNumber(aes256.encrypt(phoneNumber))
                .randomNumber(String.valueOf(randomNumber))
                .build()
        );

        return randomNumber;
    }

    /**
     * 승인 번호 검사.
     * @param phoneNumber
     * @param number
     * @return
     */
    public ResetPassword certConfirm(String phoneNumber , String number) throws Exception {

        //인증 여부 확인.
        ResetPassword resetPassword = userMapper.selectResetPasswordByCert(ResetPassword.builder()
                .phoneNumber(aes256.encrypt(phoneNumber))
                .randomNumber(number)
                .build());

        //잘못된 경우.
        if(resetPassword == null){
            throw new ServiceException("이미 인증이 만료되었거나 잘못된 전화 번호 혹은 승인번호가 올바르지 않습니다.");
        }

        //문제 없는경우 - 인증 컨펌 진행.
        userMapper.updateResetPasswordForConfirm(resetPassword.getRpId());

        return resetPassword;
    }

    /**
     * 리셋 패스워드
     * @param rpId
     * @param phoneNumber
     * @param password
     */
    public void resetPassword(Long rpId , String phoneNumber , String password){

        User selectUser = getUserByPn(phoneNumber);

        if(selectUser == null){
            throw new ServiceException("존재하지 않는 전화번호 입니다.");
        }

        ResetPassword resetPassword = userMapper.selectResetPasswordConfirmedByRpId(rpId);

        if(resetPassword == null){
            throw new ServiceException("인증을 받지 않았습니다.");
        }

        selectUser.setPassword(passwordEncoder.encode(password));
        userMapper.updateUserForPassword(selectUser);

    }
}

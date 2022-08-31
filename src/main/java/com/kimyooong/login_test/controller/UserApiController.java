package com.kimyooong.login_test.controller;

import com.kimyooong.login_test.common.RestResponse;
import com.kimyooong.login_test.domain.User;
import com.kimyooong.login_test.security.JwtTokenProvider;
import com.kimyooong.login_test.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("api/user")
public class UserApiController {

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @ApiOperation("전화 번호 인증 - 유저 디비에 전화번호가 존재하는지 확인. " +
            "\n flag 값으로 " +
            "\n 가입시 인증에 대한 사용. ( 유저 디비에 값이 없는지를 검사. )" +
            "\n 비밀번호 초기화 인지에 대한 사용. ( 유저 디비에 값이 있는지를 검사.)" +
            "\n 에 대한 여부를 결정." +
            "\n 성공 시 인증 번호를 리턴. "
    )
    @PostMapping("/cert-phone")
    public ResponseEntity<RestResponse> certPhone(@RequestBody Map<String, String> map) {

        log.info("map : {}" , map);

        return ResponseEntity.ok(RestResponse.ok());
    }

    @ApiOperation("전화 번호 인증 - 인증 번호 확인")
    @PostMapping("/cert-phone-confirm")
    public ResponseEntity<RestResponse> certPhoneConfirm(@RequestBody Map<String, String> map) {

        return ResponseEntity.ok(RestResponse.ok());
    }

    @ApiOperation("비밀번호 초기화 - 핸드폰 번호와 바꿀 패스워드 입력.")
    @PostMapping("/reset_password")
    public ResponseEntity<RestResponse> resetPassword(@RequestBody String phoneNumber) {

        return ResponseEntity.ok(RestResponse.ok());
    }

    @ApiOperation("회원 가입")
    @PostMapping("/join")
    public ResponseEntity<RestResponse> join(@RequestBody Map<String, String> user) {
        userService.setPasswordEncoder(passwordEncoder);
        userService.join(user);
        return ResponseEntity.ok(RestResponse.ok());
    }
    @ApiOperation("로그인 - 성공 시 유저 정보 및 토큰 발급. ( 전화번호 , 비밀번호 필요 ) ")
    @PostMapping("/login")
    public ResponseEntity<RestResponse> login(@RequestBody Map<String, String> user) {

        userService.setPasswordEncoder(passwordEncoder);
        User selectUser =  userService.getLogin(user.get("phone_number") , user.get("password"));
        selectUser.setJwtToken(jwtTokenProvider.createToken(selectUser.getPhoneNumber() , selectUser.getRoles()));

        return ResponseEntity.ok(RestResponse.ok(selectUser));
    }

    @ApiOperation("내 정보 불러 오기 - 토큰 인증이 필요")
    @GetMapping("/info")
    public ResponseEntity<RestResponse> info(
            HttpServletRequest request) {
        return ResponseEntity.ok(RestResponse.ok(jwtTokenProvider.getAuthentication(jwtTokenProvider.resolveToken(request)).getPrincipal()));
    }

}

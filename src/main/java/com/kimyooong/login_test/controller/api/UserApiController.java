package com.kimyooong.login_test.controller.api;

import com.kimyooong.login_test.common.RestResponse;
import com.kimyooong.login_test.domain.User;
import com.kimyooong.login_test.security.JwtTokenProvider;
import com.kimyooong.login_test.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation("전화 번호 인증 - 유저 디비에 전화번호가 존재하는지 확인.")
    @PostMapping("/cert-phone")
    public ResponseEntity<RestResponse> certPhone(
            @ApiParam("\n flag 에 따른 사용 유무 분기." +
                    "\n true - > 가입 시 인증에 대한 사용. ( 유저 디비에 값이 없는지를 검사. )" +
                    "\n false -> 비밀번호 초기화 인지에 대한 사용. ( 유저 디비에 값이 있는지를 검사.)" +
                    "\n 성공 시 인증 번호를 리턴. "+
                    "\n\n\n예시 ) " +
                    "{\n" +
                    "  \"phone_number\": \"01012341234\"\n" +
                    "  \"flag\": \"true\"\n" +
                    "}"
            )
            @RequestBody Map<String, String> map) {

        return ResponseEntity.ok(RestResponse.ok(userService.certPhoneNumber(Boolean.valueOf(map.get("flag")), map.get("phone_number"))));
    }

    @ApiOperation("전화 번호 인증 - 인증 번호 확인 ( 인증 확인이 되었으면 인증 된 리셋 패스워드 리턴 ) ")
    @PostMapping("/cert-phone-confirm")
    public ResponseEntity<RestResponse> certPhoneConfirm(
            @ApiParam(
                    "예시 ) " +
                            "{\n" +
                            "  \"phone_number\": \"01012341234\"\n" +
                            "  \"number\": \"1234\",\n" +
                    "}")@RequestBody Map<String, String> map) {

        return ResponseEntity.ok(RestResponse.ok(userService.certConfirm(map.get("phone_number") , map.get("number"))));
    }

    @ApiOperation("비밀번호 초기화 - 핸드폰 번호와 바꿀 패스워드 입력.")
    @PostMapping("/reset_password")
    public ResponseEntity<RestResponse> resetPassword(
            @ApiParam(
                    "예시 ) " +
                            "{\n" +
                            "  \"rpId\": \"인증받은 rpId\"\n" +
                            "  \"phone_number\": \"01012341234\"\n" +
                            "  \"password\": \"교체할 패스워드\",\n" +
                            "}")
            @RequestBody Map<String, String> map) {
        userService.setPasswordEncoder(passwordEncoder);
        userService.resetPassword(Long.valueOf(map.get("rpId")), map.get("phone_number") , map.get("password"));
        return ResponseEntity.ok(RestResponse.ok());
    }

    @ApiOperation("회원 가입 - 패스워드 - 단방향 암호화 , 그 외 개인정보 양방향 암호화.")
    @PostMapping("/join")
    public ResponseEntity<RestResponse> join(
            @ApiParam(
                    "예시 ) " +
                    "{\n" +
                    "  \"password\": \"1234\",\n" +
                    "  \"email\": \"test@nate.com\",\n" +
                    "  \"nick_name\": \"nick_name\",\n" +
                    "  \"name\": \"name\",\n" +
                    "  \"phone_number\": \"01012341234\"\n" +
                    "}")
            @RequestBody Map<String, String> user) throws Exception {
        userService.setPasswordEncoder(passwordEncoder);
        userService.join(user);
        return ResponseEntity.ok(RestResponse.ok());
    }
    @ApiOperation("로그인 - 성공 시 유저 정보 및 토큰 발급. ( 전화번호 , 비밀번호 필요 ) ")
    @PostMapping("/login")
    public ResponseEntity<RestResponse> login(
            @ApiParam(
                    "예시 ) " +
                            "{\n" +
                            "  \"phone_number\": \"01012341234\"\n" +
                            "  \"password\": \"1234\",\n" +
                            "}")
            @RequestBody Map<String, String> user) {

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

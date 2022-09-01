package com.kimyooong.login_test.controller;

import com.kimyooong.login_test.common.RestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Log4j2
@RequiredArgsConstructor
@Controller
public class UserController {

    @GetMapping("/test")
    public ResponseEntity<RestResponse> getTest(){
        return ResponseEntity.ok(RestResponse.ok());
    }
}



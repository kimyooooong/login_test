package com.kimyooong.login_test.controller;

import com.kimyooong.login_test.common.RestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;


@Log4j2
@RequiredArgsConstructor
@Controller
public class UserController implements ErrorController {

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/join")
    public String join(){
        return "login";
    }

    @GetMapping("/resetPassword")
    public String resetPassword(){
        return "resetPassword";
    }

    @GetMapping("/info")
    public String info(){
        return "info";
    }


    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if(status != null){
            int statusCode = Integer.parseInt(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404";
            } else if(statusCode == HttpStatus.BAD_REQUEST.value()) {
                return "error/400";
            }
        }

        return "error/500";
    }
}



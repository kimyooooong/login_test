package com.kimyooong.login_test.exception;

import com.kimyooong.login_test.common.RestResponse;
import com.kimyooong.login_test.utill.CommonMap;
import com.kimyooong.login_test.utill.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

    //서비스에서 오류나는 부분 처리.
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<RestResponse> ServiceException(Exception e){

        String errorMsg = ExceptionUtils.getStackTrace(e);
        log.error(errorMsg);
        String msg = e.getMessage();
        CommonMap map = new CommonMap();
        map.put("msg", msg);
        return ResponseEntity.ok(RestResponse.builder().success(false).data(map).build());
    }

    //그 외 서버에서 나는 모든 오류 처리.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse> intervalException(Exception e){

        String errorMsg = ExceptionUtils.getStackTrace(e);
        log.error(errorMsg);
        return ResponseEntity.internalServerError()
                .body(RestResponse.builder().success(false)
                    .data(errorMsg)
                    .build());

    }


}

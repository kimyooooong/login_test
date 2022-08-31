package com.kimyooong.login_test.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InternalServerException extends RuntimeException{

    public InternalServerException(String msg){
        super(msg);
    }

    public InternalServerException(Exception e){
        super(e);
    }
}

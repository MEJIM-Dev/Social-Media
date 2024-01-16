package com.me.social.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppUserException extends RuntimeException{

    private HttpStatus status;

    public AppUserException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }
}

package com.me.social.exception.handler;

import com.me.social.dto.response.ExceptionResponse;
import com.me.social.exception.AppUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception){
        ExceptionResponse<Map<String,String>> response = new ExceptionResponse<>();
        response.setTimeStamp(Instant.now());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMsg("Validation Error");
        List<FieldError> fieldErrors = exception.getFieldErrors();
        List<Map<String,String>> list = new ArrayList<>();
        for (FieldError fieldError: fieldErrors) {
            Map<String,String> err = new HashMap<>();
            err.put("field",fieldError.getField());
            err.put("msg",fieldError.getDefaultMessage());
            list.add(err);
        }
        response.setData(list);
        System.out.println(exception.getFieldErrors());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AppUserException.class)
    public ResponseEntity<?> userExceptionHandler(AppUserException exception){
        ExceptionResponse<Map<String,String>> response = new ExceptionResponse<>();
        response.setTimeStamp(Instant.now());
        response.setStatus(exception.getStatus().value());
        response.setMsg(exception.getMessage());
        response.setData(null);
        return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
    }
}

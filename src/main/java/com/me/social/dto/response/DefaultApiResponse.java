package com.me.social.dto.response;

import lombok.Data;

@Data
public class DefaultApiResponse <T>{
    private String status;
    private String message;
    private T data;
}

package com.me.social.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@ToString
public class ExceptionResponse <G>{
    private String msg;
    private Instant timeStamp;
    private Integer status;
    private List<G> data;
}

package com.me.social.dto.notification;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotificationDto {
    private String email;
    private String name;
    private String phone;
    private String information;
    private String subject;
    private String action;

    public NotificationDto(){}

}

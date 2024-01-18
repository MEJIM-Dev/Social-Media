package com.me.social.service;


import com.me.social.domain.Post;
import com.me.social.dto.notification.NotificationDto;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;

public interface NotificationService {

    void sendLikeNotification(NotificationDto notificationDto) throws MessagingException;

    void sendNewCommentNotification(NotificationDto notificationDto) throws MessagingException;

}

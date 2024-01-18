package com.me.social.service.impl;

import com.me.social.dto.notification.NotificationDto;
import com.me.social.service.NotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender javaMailSender;

    @Override
    @Async
    public void sendLikeNotification(NotificationDto notificationDto) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper msgHelper = new MimeMessageHelper(msg,"utf-8");

        msgHelper.setTo(notificationDto.getEmail());
        msgHelper.setSubject("Like Notification");
        msgHelper.setFrom("noreply@social-app.com");
        String template = "<div style=\" display: flex; flex-direction: column\"> <div> "+notificationDto.getName()+" Just Liked your post about: "+ notificationDto.getInformation()+" </div>";
        msgHelper.setText(template,true);

        try {
            javaMailSender.send(msg);
        } catch (MailException e){
            e.printStackTrace();
            log.info("Failed to Send Mail");
            return;
        }
        log.info("Sent Successfully");
    }

    @Override
    @Async
    public void sendNewCommentNotification(NotificationDto notificationDto) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper msgHelper = new MimeMessageHelper(msg,"utf-8");

        msgHelper.setTo(notificationDto.getEmail());
        msgHelper.setSubject("New Comment");
        msgHelper.setFrom("noreply@social-app.com");
        String template = "<div style=\" display: flex; flex-direction: column\"> <div> "+notificationDto.getName()+" Just Said: "+notificationDto.getInformation()+" on your post about."+" </div>";
        msgHelper.setText(template,true);

        try {
            javaMailSender.send(msg);
        } catch (MailException e){
            e.printStackTrace();
            log.info("Failed to Send Mail");
            return;
        }
        log.info("Sent Successfully");
    }


}

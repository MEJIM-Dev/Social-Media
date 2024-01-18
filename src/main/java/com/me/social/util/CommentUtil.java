package com.me.social.util;

import com.me.social.domain.Comment;
import com.me.social.domain.Post;
import com.me.social.domain.User;
import com.me.social.dto.notification.NotificationDto;
import com.me.social.dto.response.CommentResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class CommentUtil {

    public CommentResponseDTO commentToDto(Comment comment) {
        CommentResponseDTO responseDTO = new CommentResponseDTO();
        responseDTO.setId(comment.getId());
        responseDTO.setContent(comment.getContent());
        responseDTO.setDeleted(comment.isDeleted());
        responseDTO.setCreationDate(comment.getCreationDate());
        return responseDTO;
    }

    public NotificationDto generateNewCommentNotificationDto(Comment comment, User user, Post post) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setName(user.getFirstName());
        String content = comment.getContent();
        String info = content.length()>20 ? String.format("%s....", content.substring(20)): content;
        notificationDto.setInformation(info);
        notificationDto.setEmail(post.getUser().getEmail());
        return notificationDto;
    }
}

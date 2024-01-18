package com.me.social.util;

import com.me.social.domain.Post;
import com.me.social.domain.User;
import com.me.social.dto.notification.NotificationDto;
import com.me.social.dto.response.PostResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class PostUtil {

    public PostResponseDTO postToDto(Post post){
        PostResponseDTO responseDTO = new PostResponseDTO();
        responseDTO.setId(post.getId());
        responseDTO.setContent(post.getContent());
        responseDTO.setCreationDate(post.getCreationDate());
        responseDTO.setDeleted(post.isDeleted());
        responseDTO.setLikesCount(post.getLikesCount());
        responseDTO.setUserId(post.getUser().getId());
        responseDTO.setComments(post.getComments().size());
        return responseDTO;
    }

    public NotificationDto generateLikeNotificationDto(Post post, User user){
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setEmail(post.getUser().getEmail());
        notificationDto.setName(user.getFirstName());
        String content = post.getContent();
        String info = content.length()>20 ? String.format("%s...",content.substring(0,20)): content;
        notificationDto.setInformation(info);
        return notificationDto;
    }

    public NotificationDto generatePostNotificationDto(Post post, User user){
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setEmail(user.getEmail());
        notificationDto.setName(post.getUser().getFirstName());
        String content = post.getContent();
        String info = content.length()>20 ? String.format("%s...",content.substring(0,20)): content;
        notificationDto.setInformation(info);
        return notificationDto;
    }
}

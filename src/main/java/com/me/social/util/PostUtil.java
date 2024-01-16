package com.me.social.util;

import com.me.social.domain.Post;
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
        return responseDTO;
    }
}

package com.me.social.util;

import com.me.social.domain.Comment;
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
}

package com.kachina.post_service.mapper;

import java.time.LocalDateTime;

import com.kachina.post_service.dto.request.PostCreationRequest;
import com.kachina.post_service.dto.response.PostResponse;
import com.kachina.post_service.entity.Post;

public class PostMapper {
    
    public static Post toPost(PostCreationRequest request) {
        return Post.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    public static PostResponse toPostResponse(Post post) {
        return PostResponse.builder()
            .id(post.getId())
            .userId(post.getUserId())
            .title(post.getTitle())
            .content(post.getContent())
            .createdAt(post.getCreatedAt())
            .updatedAt(post.getUpdatedAt())
            .build();
    }

}

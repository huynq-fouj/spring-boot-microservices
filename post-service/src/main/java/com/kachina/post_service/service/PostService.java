package com.kachina.post_service.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kachina.post_service.dto.request.PostCreationRequest;
import com.kachina.post_service.dto.response.PostResponse;
import com.kachina.post_service.entity.Post;
import com.kachina.post_service.mapper.PostMapper;
import com.kachina.post_service.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    
    private final PostRepository postRepository;

    public PostResponse createPost(PostCreationRequest request, String userId){

        Post post = PostMapper.toPost(request);
        post.setUserId(userId);
        post.setCreatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);

        return PostMapper.toPostResponse(savedPost);
    }

    public PostResponse getPostById(String id) {
        Optional<Post> optPost = postRepository.findById(id);
        if(!optPost.isPresent()) {
            throw new RuntimeException("Post not found with id: " + id);
        }
        return PostMapper.toPostResponse(optPost.get());
    }

    public Map<String, Object> getPosts(Pageable pageable) {

        Page<Post> pagePosts = postRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("currentPage", pagePosts.getNumber());
        response.put("totalPage", pagePosts.getTotalPages());
        response.put("totalItems", pagePosts.getTotalElements());
        response.put("posts", pagePosts.getContent()
            .stream()
            .map(PostMapper::toPostResponse)
            .collect(Collectors.toList())
        );

        return response;
    }

    public Map<String, Object> getPostByUserId(String userId, Pageable pageable) {

        Page<Post> pagePosts = postRepository.findByUserId(userId, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("currentPage", pagePosts.getNumber());
        response.put("totalPage", pagePosts.getTotalPages());
        response.put("totalItems", pagePosts.getTotalElements());
        response.put("posts", pagePosts.getContent()
            .stream()
            .map(PostMapper::toPostResponse)
            .collect(Collectors.toList())
        );

        return response;
    }

}

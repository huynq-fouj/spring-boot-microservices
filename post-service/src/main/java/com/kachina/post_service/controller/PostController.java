package com.kachina.post_service.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kachina.post_service.dto.request.PostCreationRequest;
import com.kachina.post_service.dto.response.ApiResponse;
import com.kachina.post_service.dto.response.PostResponse;
import com.kachina.post_service.jwt.JwtUtils;
import com.kachina.post_service.service.PostService;
import com.kachina.post_service.utils.AuthUtils;
import com.kachina.post_service.utils.SortUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
// @RequestMapping("/")
@Slf4j
public class PostController {
    
    private final PostService postService;
    private final JwtUtils jwtUtils;

    @PostMapping("/create")
    public ApiResponse<PostResponse> createPost(
        @RequestHeader("Authorization") String token,
        @RequestBody PostCreationRequest request
    ) {

        String jwt = AuthUtils.parseJwt(token);
        String userId = jwtUtils.getUserId(jwt);

        return ApiResponse.<PostResponse>builder()
            .code((short) 201)
            .result(postService.createPost(request, userId))
        .build();
    }

    @GetMapping("/p/{postId}")
    public ApiResponse<PostResponse> getPostById(@PathVariable("postId") String id) {
        return ApiResponse.<PostResponse>builder()
            .code((short) 200)
            .result(postService.getPostById(id))
        .build();
    }

    @GetMapping("/list")
    public ApiResponse<Map<String, Object>> getPosts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "createdAt:desc") String sort
    ) {

        List<Order> orders = new ArrayList<>();
        try {
            String[] _sort = sort.split(":");
            orders.add(new Order(SortUtils.getSortDirection(_sort[1]), _sort[0]));
        } catch (Exception e) {
            return ApiResponse.<Map<String, Object>>builder()
                .code((short) 400)
                .message(e.getMessage())
            .build();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        return ApiResponse.<Map<String, Object>>builder()
            .code((short) 200)
            .result(postService.getPosts(pageable))
        .build();
    }

    @GetMapping("/u/{userId}")
    public ApiResponse<Map<String, Object>> getPostsByUser(
        @PathVariable("userId") String id,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "createdAt:desc") String sort
    ) {

        List<Order> orders = new ArrayList<>();
        try {
            String[] _sort = sort.split(":");
            orders.add(new Order(SortUtils.getSortDirection(_sort[1]), _sort[0]));
        } catch (Exception e) {
            return ApiResponse.<Map<String, Object>>builder()
                .code((short) 400)
                .message(e.getMessage())
            .build();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        return ApiResponse.<Map<String, Object>>builder()
            .code((short) 200)
            .result(postService.getPostByUserId(id, pageable))
        .build();
    }

}

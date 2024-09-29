package com.kachina.post_service.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kachina.post_service.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {

    Page<Post> findByUserId(String userId, Pageable pageable);

    // Page<Post> findByUserId(String userId, Pageable pageable);

}

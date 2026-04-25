package com.example.gaurdsocial.repository;

import com.example.gaurdsocial.entity.Like;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LikeRepository extends CrudRepository<Like, Long> {
    boolean existsByPostPostIdAndAuthorIdAndAuthorType(Long postId, Long authorId,String authorType);
    Optional<Like> findByPostPostIdAndAuthorIdAndAuthorType(Long postId, Long authorId, String authorType);
}

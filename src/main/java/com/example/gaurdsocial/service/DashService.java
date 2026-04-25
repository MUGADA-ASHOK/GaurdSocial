package com.example.gaurdsocial.service;

import com.example.gaurdsocial.dto.CommentRequest;
import com.example.gaurdsocial.dto.LikeRequest;
import com.example.gaurdsocial.dto.PostRequest;

public interface DashService {
    String addPost(PostRequest postRequest);
    String addComment(Long postId,CommentRequest commentRequest);
    String likePost(Long postId, LikeRequest likeRequest);
    String unlikePost(Long postId,LikeRequest likeRequest);
}

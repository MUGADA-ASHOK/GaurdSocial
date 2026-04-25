package com.example.gaurdsocial.service.impl;

import com.example.gaurdsocial.dto.CommentRequest;
import com.example.gaurdsocial.dto.LikeRequest;
import com.example.gaurdsocial.dto.PostRequest;
import com.example.gaurdsocial.entity.Comment;
import com.example.gaurdsocial.entity.Like;
import com.example.gaurdsocial.entity.Post;
import com.example.gaurdsocial.exception.ResourceNotFoundException;
import com.example.gaurdsocial.repository.BotRepository;
import com.example.gaurdsocial.repository.CommentRepository;
import com.example.gaurdsocial.repository.PostRepository;
import com.example.gaurdsocial.repository.UserRepository;
import com.example.gaurdsocial.service.DashService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@AllArgsConstructor
@Service
@Transactional
public class DashServiceImpl implements DashService {
    private final BotRepository botRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    @Override
    public String addPost(PostRequest postRequest) {
        Post post = new Post();
        if(postRequest.getIsUser()) {
            if(!botRepository.findById(postRequest.getAuthorId()).isPresent()) {
                return "author id is not exists to add a post";
            }
            post.setAuthorType("bot");
        }
        else{
            if(!userRepository.findById(postRequest.getAuthorId()).isPresent()) {
                return "author id is not exists to add a post";
            }
            post.setAuthorType("user");
        }
        post.setAuthorId(postRequest.getAuthorId());
        post.setCreatedAt(LocalDateTime.now());
        post.setContent(postRequest.getContent());
        return "Post added successfully";
    }

    @Override
    public String addComment(Long postId,CommentRequest commentRequest) {
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null) {
            return new ResourceNotFoundException("post not found").toString();
        }
        Comment comment = new Comment();
        if(commentRequest.getParentCommentId()!=null) {
            Comment parentComment = commentRepository.findById(commentRequest.getParentCommentId()).orElse(null);
            if(parentComment == null) {
                return new ResourceNotFoundException("parent comment not found").toString();
            }
            else{
                Long level = parentComment.getDepthLevel();
                if(level==null){
                    level = 0L;
                }
                if(level<20) {
                    comment.setDepthLevel(level + 1);
                    comment.setParentCommentId(commentRequest.getParentCommentId());
                    comment.setCreatedAt(LocalDateTime.now());
                    comment.setPost(post);
                }
                else{
                    return "please comment the level less than 20";
                }
            }
        }
        return "Comment added successfully";
    }

    @Override
    public String likePost(Long postId, LikeRequest likeRequest) {
        Post  post = postRepository.findById(postId).orElse(null);
        if(post == null) {
            return new ResourceNotFoundException("post not found").toString();
        }
        Like like = new Like();
        like.setAuthorId(likeRequest.getAuthorId());
        like.setLiked(true);
        like.setAuthorType(likeRequest.getAuthorType());
        like.setPost(post);
        return likeRequest.getAuthorId()+ "liked successfully to"+postId;
    }

    @Override
    public String unlikePost(Long postId,LikeRequest likeRequest) {
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null) {
            return new ResourceNotFoundException("post not found").toString();
        }
        Like like = new Like();
        like.setLiked(false);
        like.setAuthorId(likeRequest.getAuthorId());
        like.setPost(post);
        return likeRequest.getAuthorId()+ "unliked successfully to"+postId;
    }

}

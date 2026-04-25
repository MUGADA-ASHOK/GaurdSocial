package com.example.gaurdsocial.service.impl;

import com.example.gaurdsocial.dto.CommentRequest;
import com.example.gaurdsocial.dto.LikeRequest;
import com.example.gaurdsocial.dto.PostRequest;
import com.example.gaurdsocial.entity.Comment;
import com.example.gaurdsocial.entity.Like;
import com.example.gaurdsocial.entity.Post;
import com.example.gaurdsocial.exception.ResourceNotFoundException;
import com.example.gaurdsocial.exception.RateLimitException;
import com.example.gaurdsocial.repository.*;
import com.example.gaurdsocial.service.DashService;
import com.example.gaurdsocial.service.RedisService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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
    private final LikeRepository likeRepository;
    private final RedisService redisService; // only addition

    @Override
    public String addPost(PostRequest postRequest) {
        Post post = new Post();
        boolean isBot = postRequest.getAuthorType().equals("bot");
        if (isBot) {
            if (!botRepository.findById(postRequest.getAuthorId()).isPresent()) {
                return "author id is not exists to add a post";
            }
            post.setAuthorType("bot");
        } else {
            if (!userRepository.findById(postRequest.getAuthorId()).isPresent()) {
                return "author id is not exists to add a post";
            }
            post.setAuthorType("user");
        }
        post.setAuthorId(postRequest.getAuthorId());
        post.setCreatedAt(LocalDateTime.now());
        post.setContent(postRequest.getContent());
        postRepository.save(post);
        return "Post added successfully";
    }

    @Override
    public String addComment(Long postId, CommentRequest commentRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post not found"));

        boolean isBot = "bot".equals(commentRequest.getAuthorType());

        if (isBot) {
            Long botId = commentRequest.getAuthorId();
            Long postOwnerId = post.getAuthorId();
            boolean postOwnerIsHuman = "user".equals(post.getAuthorType());
            if (postOwnerIsHuman && redisService.isBotOnCooldown(botId, postOwnerId)) {
                throw new RateLimitException("Bot " + botId + " is on cooldown for this user. Try after 10 minutes.");
            }
            Long botCount = redisService.incrementBotCount(postId);
            if (botCount > 100) {
                redisService.decrementBotCount(postId);
                throw new RateLimitException("Post " + postId + " has reached the 100 bot comment limit.");
            }
            if (postOwnerIsHuman) {
                redisService.setBotCooldown(botId, postOwnerId);
                redisService.handleBotNotification(botId, postOwnerId, postId);
            }
            redisService.addViralityScore(postId, 1);
        } else {
            redisService.addViralityScore(postId, 50);
        }
        Comment comment = new Comment();
        if (commentRequest.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(commentRequest.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("parent comment not found"));
            Long level = parentComment.getDepthLevel();
            if (level == null) level = 0L;

            if (level < 20) {
                comment.setDepthLevel(level + 1);
                comment.setParentCommentId(commentRequest.getParentCommentId());
                comment.setCreatedAt(LocalDateTime.now());
                comment.setPost(post);
                comment.setAuthorType(commentRequest.getAuthorType());
                comment.setAuthorId(commentRequest.getAuthorId());
                comment.setContent(commentRequest.getContent());
            } else {
                return "comment level exceeds 20 levels, you can't comment";
            }
        } else {
            comment.setDepthLevel(1L);
            comment.setParentCommentId(postId);
            comment.setCreatedAt(LocalDateTime.now());
            comment.setPost(post);
            comment.setAuthorType(commentRequest.getAuthorType());
            comment.setAuthorId(commentRequest.getAuthorId());
            comment.setContent(commentRequest.getContent());
        }

        commentRepository.save(comment);
        return commentRequest.getAuthorType() + " " + commentRequest.getAuthorId()
                + " Added Comment to " + comment.getParentCommentId()
                + " Successfully and the level = " + comment.getDepthLevel();
    }

    @Override
    public String likePost(Long postId, LikeRequest likeRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post not found"));
        if ("user".equals(likeRequest.getAuthorType())) {
            redisService.addViralityScore(postId, 20);
        }
        Like savedLike = likeRepository.findByPostPostIdAndAuthorIdAndAuthorType(
                postId, likeRequest.getAuthorId(), likeRequest.getAuthorType()).orElse(null);

        if (savedLike == null) {
            Like like = new Like();
            like.setPost(post);
            like.setAuthorId(likeRequest.getAuthorId());
            like.setAuthorType(likeRequest.getAuthorType());
            like.setLiked(true);
            likeRepository.save(like);
        } else if (savedLike.getLiked()) {
            return likeRequest.getAuthorType() + " " + likeRequest.getAuthorId() + " already liked the post";
        } else {
            savedLike.setLiked(true);
            likeRepository.save(savedLike);
        }

        return likeRequest.getAuthorType() + " " + likeRequest.getAuthorId()
                + " liked successfully to postId " + postId;
    }

    @Override
    public String unlikePost(Long postId, LikeRequest likeRequest) {
        postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post not found" + postId));

        Like like = likeRepository.findByPostPostIdAndAuthorIdAndAuthorType(
                        postId, likeRequest.getAuthorId(), likeRequest.getAuthorType())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "post not found with authorId " + likeRequest.getAuthorId()
                                + " first like the post to unlike it "));

        like.setLiked(false);
        likeRepository.save(like);
        return likeRequest.getAuthorType() + " " + likeRequest.getAuthorId()
                + " unliked successfully to postId " + postId;
    }
}
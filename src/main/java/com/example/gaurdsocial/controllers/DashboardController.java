package com.example.gaurdsocial.controllers;

import com.example.gaurdsocial.dto.CommentRequest;
import com.example.gaurdsocial.dto.LikeRequest;
import com.example.gaurdsocial.dto.PostRequest;
import com.example.gaurdsocial.service.DashService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/posts")
public class DashboardController {

    private final DashService dashService;
    @PostMapping
    public ResponseEntity<String> addPost(@RequestBody PostRequest postRequest) {
        return  ResponseEntity.ok().body(dashService.addPost(postRequest));
    }
    @PostMapping("/{postId}/comments")
    public ResponseEntity<String> addComment(@PathVariable("postId") Long postId, @RequestBody CommentRequest commentRequest) {
        return  ResponseEntity.ok().body(dashService.addComment(postId,commentRequest));
    }
    @PutMapping("/{postId}/like")
    public ResponseEntity<String>likePost(@PathVariable("postId") Long postId, @RequestBody LikeRequest likeRequest) {
        return ResponseEntity.ok().body(dashService.likePost(postId,likeRequest));
    }
    @PutMapping("/{postId}/unlike")
    public ResponseEntity<String>unlikePost(@PathVariable("postId") Long postId, @RequestBody LikeRequest likeRequest) {
        return ResponseEntity.ok().body(dashService.unlikePost(postId,likeRequest));
    }
}

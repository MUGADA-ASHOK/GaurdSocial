package com.example.gaurdsocial.service;

import com.example.gaurdsocial.config.RedisKeys;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@AllArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final Logger log = LoggerFactory.getLogger(RedisService.class);
    public Long incrementBotCount(Long postId) {
        return redisTemplate.opsForValue().increment(RedisKeys.botCount(postId));
    }

    public void decrementBotCount(Long postId) {
        redisTemplate.opsForValue().decrement(RedisKeys.botCount(postId));
    }

    public boolean isBotOnCooldown(Long botId, Long humanId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeys.botCooldown(botId, humanId)));
    }

    public void setBotCooldown(Long botId, Long humanId) {
        redisTemplate.opsForValue().set(
                RedisKeys.botCooldown(botId, humanId),
                "1",
                Duration.ofMinutes(10)
        );
    }
    public void addViralityScore(Long postId, long points) {
        redisTemplate.opsForValue().increment(RedisKeys.viralityScore(postId), points);
    }

    public boolean isNotifOnCooldown(Long userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeys.notifCooldown(userId)));
    }

    public void setNotifCooldown(Long userId) {
        redisTemplate.opsForValue().set(
                RedisKeys.notifCooldown(userId),
                "1",
                Duration.ofMinutes(15)
        );
    }

    public void pushPendingNotif(Long userId, String message) {
        redisTemplate.opsForList().leftPush(RedisKeys.pendingNotifs(userId), message);
    }

    public List<String> getPendingNotifs(Long userId) {
        return redisTemplate.opsForList().range(RedisKeys.pendingNotifs(userId), 0, -1);
    }
    public void clearPendingNotifs(Long userId) {
        redisTemplate.delete(RedisKeys.pendingNotifs(userId));
    }
    public void handleBotNotification(Long botId, Long userId, Long postId) {
        String message = "Bot " + botId + " replied to your post " + postId;
        if (isNotifOnCooldown(userId)) {
            pushPendingNotif(userId, message);
        } else {
            log.info("Push Notification Sent to User {}", userId);
            setNotifCooldown(userId);
        }
    }
}
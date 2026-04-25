package com.example.gaurdsocial.config;

public class RedisKeys {

    public static String botCount(Long postId) {
        return "post:" + postId + ":bot_count";
    }

    public static String viralityScore(Long postId) {
        return "post:" + postId + ":virality_score";
    }

    public static String botCooldown(Long botId, Long humanId) {
        return "cooldown:bot_" + botId + ":human_" + humanId;
    }

    public static String notifCooldown(Long userId) {
        return "notif_cooldown:user_" + userId;
    }

    public static String pendingNotifs(Long userId) {
        return "user:" + userId + ":pending_notifs";
    }
}
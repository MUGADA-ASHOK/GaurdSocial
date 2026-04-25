package com.example.gaurdsocial.scheduler;

import com.example.gaurdsocial.repository.UserRepository;
import com.example.gaurdsocial.service.RedisService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
@AllArgsConstructor
public class NotificationScheduler {

    private final UserRepository userRepository;
    private final RedisService redisService;
    private static final Logger log = LoggerFactory.getLogger(NotificationScheduler.class);

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void sweepPendingNotifications() {
        userRepository.findAll().forEach(user -> {
            Long userId = user.getAuthorId();
            List<String> notifs = redisService.getPendingNotifs(userId);
            if (notifs != null && !notifs.isEmpty()) {
                String firstMessage = notifs.get(0);
                int othersCount = notifs.size() - 1;
                log.info("Summarized Push Notification: {} and [{}] others interacted with posts.",
                        firstMessage, othersCount);
                redisService.clearPendingNotifs(userId);
            }
        });
    }
}
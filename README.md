# GuardSocial

## Objective
A Spring Boot microservice for social interactions with Redis-based 
guardrails to prevent bot spam and AI compute runaway.

## Tech Stack
Java 17, Spring Boot 3.x, PostgreSQL, Redis, Maven, Lombok, Docker

## Setup Steps

1. Clone the repository
2. Copy application-prod.properties.example to application-prod.properties
3. Fill in your actual DB credentials
4. Copy .env.example to .env and fill in your credentials
5. Run: docker-compose up -d
6. Run the Spring Boot app

## API Endpoints
POST   /api/user
POST   /api/bot
POST   /api/posts
POST   /api/posts/{postId}/comments
PUT    /api/posts/{postId}/like
PUT    /api/posts/{postId}/unlike

## Redis Keys
post:{id}:bot_count          → bot comment counter per post
post:{id}:virality_score     → real-time virality score
cooldown:bot_{id}:human_{id} → bot-to-human cooldown (10 min TTL)
notif_cooldown:user_{id}     → notification cooldown (15 min TTL)
user:{id}:pending_notifs     → queued notifications list

## Thread Safety
Redis INCR is atomic. Even with 200 concurrent requests each gets a 
unique count. Once counter crosses 100 all further requests are rejected 
before touching PostgreSQL.

## Testing 100 Bot Comment Limit
Use Postman Collection Runner on the bot comment endpoint,
set iterations to 200, delay 0ms. Database will show exactly 100 rows.

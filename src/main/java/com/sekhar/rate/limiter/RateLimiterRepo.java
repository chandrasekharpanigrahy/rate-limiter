package com.sekhar.rate.limiter;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RateLimiterRepo extends MongoRepository<RateLimiter, String> {
    List<RateLimiter> findAllByClientIdAndRequestTimeBetween(String clientId, LocalDateTime from, LocalDateTime to);
}

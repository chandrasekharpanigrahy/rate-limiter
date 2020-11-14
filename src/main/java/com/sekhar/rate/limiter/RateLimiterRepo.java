package com.sekhar.rate.limiter;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RateLimiterRepo extends MongoRepository<RateLimiter, String> {
    List<RateLimiter> findAllByClientId(String clientId);

    void deleteByClientIdAndRequestTimeLessThan(String clientId, LocalDateTime requestTime);
}

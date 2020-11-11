package com.sekhar.rate.limiter;

import java.time.LocalDateTime;

public class RateLimiterService {
    private RateLimiterRepo rateLimiterRepo;
    public RateLimiterService(RateLimiterRepo rateLimiterRepo){
        this.rateLimiterRepo = rateLimiterRepo;
    }
    
    public void updateForRequest(String clientId){
        rateLimiterRepo.save(new RateLimiter(clientId, LocalDateTime.now()));
    }

    // need to delete old records
    public boolean isLimitExceeded(String clientId, Long limit, Long seconds){
        LocalDateTime now = LocalDateTime.now();
        return rateLimiterRepo.findAllByClientIdAndRequestTimeBetween(clientId, now.minusSeconds(seconds), now).size() > limit;
    }
}

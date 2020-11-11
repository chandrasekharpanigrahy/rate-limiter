package com.sekhar.rate.limiter;

import java.time.LocalDateTime;

public class RateLimiterService {
    private RateLimiterRepo rateLimiterRepo;

    private RateLimiterProperty property;

    public RateLimiterService(RateLimiterRepo rateLimiterRepo, RateLimiterProperty rateLimiterProperty){
        this.rateLimiterRepo = rateLimiterRepo;
        this.property = rateLimiterProperty;
    }
    
    public void updateForRequest(String clientId){
        rateLimiterRepo.save(new RateLimiter(clientId, LocalDateTime.now()));
    }

    // need to delete old records
    public boolean isLimitExceeded(String clientId){
        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from;
        String unit = property.getUnit();
        if ("SECOND".equalsIgnoreCase(unit)) from = to.minusSeconds(1);
        else if("MINUTE".equalsIgnoreCase(unit)) from = to.minusMinutes(1);
        else from = to.minusHours(1);

        return rateLimiterRepo.findAllByClientIdAndRequestTimeBetween(clientId, from, to).size() > property.getLimit();
    }
}

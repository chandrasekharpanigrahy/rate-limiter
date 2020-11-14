package com.sekhar.rate.limiter;

import java.time.Clock;
import java.time.LocalDateTime;

public class RateLimiterService {

    private final Clock clock;

    private final RateLimiterRepo rateLimiterRepo;

    private final RateLimiterProperty property;

    public RateLimiterService(RateLimiterRepo rateLimiterRepo, RateLimiterProperty rateLimiterProperty, Clock clock) {
        this.rateLimiterRepo = rateLimiterRepo;
        this.property = rateLimiterProperty;
        this.clock = clock;
    }

    public void updateForRequest(String clientId) {
        rateLimiterRepo.save(new RateLimiter(clientId, LocalDateTime.now(clock)));
    }

    public boolean isLimitExceeded(String clientId) {
        return rateLimiterRepo.findAllByClientId(clientId).size() >= property.getLimit();
    }

    void deleteEntityLessThanWindowTime(String clientId) {
        LocalDateTime now = LocalDateTime.now(clock);
        String unit = property.getUnit();
        LocalDateTime from;
        if ("SECOND".equalsIgnoreCase(unit)) from = now.minusSeconds(1);
        else if ("MINUTE".equalsIgnoreCase(unit)) from = now.minusMinutes(1);
        else from = now.minusHours(1);
        rateLimiterRepo.deleteByClientIdAndRequestTimeLessThan(clientId, from);
    }
}

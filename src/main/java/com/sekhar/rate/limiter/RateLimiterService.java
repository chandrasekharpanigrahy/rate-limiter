package com.sekhar.rate.limiter;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;


public class RateLimiterService {

    private final Clock clock;

    private final StringRedisTemplate rateLimiterRepo;

    private final RateLimiterProperty property;

    public RateLimiterService(StringRedisTemplate rateLimiterRepo, RateLimiterProperty rateLimiterProperty, Clock clock) {
        this.rateLimiterRepo = rateLimiterRepo;
        this.property = rateLimiterProperty;
        this.clock = clock;
    }

    public void updateForRequest(String clientId) {
        ImmutablePair<String, TimeUnit> keyToUnit = getKeyAndUnit(clientId);
        ValueOperations<String, String> values = rateLimiterRepo.opsForValue();
        values.increment(keyToUnit.getLeft(), 1);
        rateLimiterRepo.expire(keyToUnit.getLeft(), 1, keyToUnit.getRight());
    }

    public boolean isLimitExceeded(String clientId) {
        ValueOperations<String, String> values = rateLimiterRepo.opsForValue();
        String value = values.get(getKeyAndUnit(clientId).getLeft());
        if (value == null) return false;
        return Long.parseLong(value) >= property.getLimit();
    }

    private ImmutablePair<String, TimeUnit> getKeyAndUnit(String clientId) {
        LocalDateTime now = LocalDateTime.now(clock);
        String unit = property.getUnit();
        TimeUnit expireUnit;
        int keyAppender;
        if ("SECOND".equalsIgnoreCase(unit)) {
            expireUnit = TimeUnit.SECONDS;
            keyAppender = now.getSecond();
        }
        else if ("MINUTE".equalsIgnoreCase(unit)) {
            expireUnit = TimeUnit.MINUTES;
            keyAppender = now.getMinute();
        }
        else {
            expireUnit = TimeUnit.HOURS;
            keyAppender = now.getHour();
        }
        return new ImmutablePair<>(clientId + property.getUnit() + keyAppender, expireUnit);
    }
}

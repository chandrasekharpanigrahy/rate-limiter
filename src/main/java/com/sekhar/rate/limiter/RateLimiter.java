package com.sekhar.rate.limiter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RateLimiter {
    private String clientId;
    private LocalDateTime requestTime;
}

package com.sekhar.rate.limiter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RateLimiterProperty {
    private String unit = "SECOND";
    private Long limit = 2L;
}

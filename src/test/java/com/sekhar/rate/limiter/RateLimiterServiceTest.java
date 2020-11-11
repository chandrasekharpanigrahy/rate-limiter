package com.sekhar.rate.limiter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RateLimiterServiceTest {

    @Autowired
    private RateLimiterService rateLimiterService;

    @Autowired
    private RateLimiterProperty property;

    @Autowired
    private RateLimiterRepo repo;

    @Test
    public void should_update_rateLimiter(){
        rateLimiterService.updateForRequest("client1");
        assertFalse(repo.findAll().isEmpty());
    }

    @Test
    public void should_return_limit_exceded_as_true_when_multiple_updates_are_done_in_few_seconds() {
        property.setLimit(2L);
        property.setUnit("SECOND");
        rateLimiterService.updateForRequest("client1");
        rateLimiterService.updateForRequest("client1");
        rateLimiterService.updateForRequest("client1");
        assertTrue(rateLimiterService.isLimitExceeded("client1"));
    }
}
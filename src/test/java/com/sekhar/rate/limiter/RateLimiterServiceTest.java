package com.sekhar.rate.limiter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;

import static java.time.Clock.fixed;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RateLimiterServiceTest {

    private final RateLimiterProperty property = new RateLimiterProperty();
    private final RateLimiterRepo repo = mock(RateLimiterRepo.class);
    private final Clock clock = mock(Clock.class);
    private RateLimiterService rateLimiterService;

    @BeforeEach
    void setUp() {
        property.setLimit(1L);
        property.setUnit("SECOND");
        Clock novemeberClock = fixed(LocalDateTime.of(2020, 11, 14, 10, 5, 10, 0)
                .toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
        when(clock.instant()).thenReturn(Instant.now(novemeberClock));
        when(clock.millis()).thenReturn(novemeberClock.millis());
        when(clock.getZone()).thenReturn(novemeberClock.getZone());
        rateLimiterService = new RateLimiterService(repo, property, clock);
    }

    @Test
    public void should_update_rateLimiter_repo() {
        rateLimiterService.updateForRequest("client1");
        verify(repo, times(1)).save(any(RateLimiter.class));
    }

    @Test
    public void should_return_limit_exceded_as_true_client_has_more_request_then_permitted_limit() {
        when(repo.findAllByClientId("client1"))
                .thenReturn(List.of(new RateLimiter("client1", LocalDateTime.now()), new RateLimiter("client1", LocalDateTime.now().minusNanos(100))));
        assertTrue(rateLimiterService.isLimitExceeded("client1"));
    }

    @Test
    public void should_delete_records_less_then_current_date_minus_one_second_if_unit_is_second() {
        rateLimiterService.deleteEntityLessThanWindowTime("client1");
        Clock clock = fixed(Instant.parse("2020-11-14T10:05:09Z"), ZoneId.of("UTC"));
        verify(repo, times(1)).deleteByClientIdAndRequestTimeLessThan("client1", LocalDateTime.now(clock));
    }

    @Test
    public void should_delete_records_less_then_current_date_minus_one_MINUTE_if_unit_is_MINUTE() {
        property.setUnit("MINUTE");
        rateLimiterService.deleteEntityLessThanWindowTime("client1");
        Clock clock = fixed(Instant.parse("2020-11-14T10:04:10Z"), ZoneId.of("UTC"));
        verify(repo, times(1)).deleteByClientIdAndRequestTimeLessThan("client1", LocalDateTime.now(clock));
    }

    @Test
    public void should_delete_records_less_then_current_date_minus_one_hour_if_unit_is_null() {
        property.setUnit(null);
        rateLimiterService.deleteEntityLessThanWindowTime("client1");
        Clock clock = fixed(Instant.parse("2020-11-14T09:05:10Z"), ZoneId.of("UTC"));
        verify(repo, times(1)).deleteByClientIdAndRequestTimeLessThan("client1", LocalDateTime.now(clock));
    }
}
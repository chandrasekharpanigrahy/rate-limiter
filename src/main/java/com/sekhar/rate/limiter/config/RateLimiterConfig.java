package com.sekhar.rate.limiter.config;

import com.sekhar.rate.limiter.RateLimiterFilter;
import com.sekhar.rate.limiter.RateLimiterRepo;
import com.sekhar.rate.limiter.RateLimiterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {

    @Bean
    @ConditionalOnMissingBean(name = "rateLimiterFilter")
    public FilterRegistrationBean rateLimiterFilter(RateLimiterService rateLimiterService) {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new RateLimiterFilter(rateLimiterService));
        bean.setOrder(Integer.MAX_VALUE);
        return bean;
    }

    @Bean
    public RateLimiterService rateLimiterService(RateLimiterRepo rateLimiterRepo) {
        return new RateLimiterService(rateLimiterRepo);
    }

}

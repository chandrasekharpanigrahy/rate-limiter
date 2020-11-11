package com.sekhar.rate.limiter.config;

import com.sekhar.rate.limiter.RateLimiterFilter;
import com.sekhar.rate.limiter.RateLimiterProperty;
import com.sekhar.rate.limiter.RateLimiterRepo;
import com.sekhar.rate.limiter.RateLimiterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "rate-limiter", name = "enabled", havingValue = "true", matchIfMissing = true)
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
    public RateLimiterService rateLimiterService(RateLimiterRepo rateLimiterRepo, RateLimiterProperty rateLimiterProperty) {
        return new RateLimiterService(rateLimiterRepo, rateLimiterProperty);
    }

    @Bean
    @ConfigurationProperties("rate-limiter")
    public RateLimiterProperty rateLimiterProperty(){
        return new RateLimiterProperty();
    }

}

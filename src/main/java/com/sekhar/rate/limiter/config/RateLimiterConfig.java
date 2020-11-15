package com.sekhar.rate.limiter.config;

import com.sekhar.rate.limiter.RateLimiterFilter;
import com.sekhar.rate.limiter.RateLimiterProperty;
import com.sekhar.rate.limiter.RateLimiterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Clock;

@Configuration
@ConditionalOnProperty(prefix = "rate-limiter", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RateLimiterConfig {
    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    @ConditionalOnMissingBean(name = "rateLimiterFilter")
    public FilterRegistrationBean rateLimiterFilter(RateLimiterService rateLimiterService) {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new RateLimiterFilter(rateLimiterService));
        bean.setOrder(Integer.MAX_VALUE);
        return bean;
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        return factory;
    }

    @Bean
    public StringRedisTemplate rateLimiterRepo() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    @Bean
    public RateLimiterService rateLimiterService(StringRedisTemplate rateLimiterRepo, RateLimiterProperty rateLimiterProperty, Clock clock) {
        return new RateLimiterService(rateLimiterRepo, rateLimiterProperty, clock);
    }

    @Bean
    @ConfigurationProperties("rate-limiter")
    public RateLimiterProperty rateLimiterProperty() {
        return new RateLimiterProperty();
    }

}

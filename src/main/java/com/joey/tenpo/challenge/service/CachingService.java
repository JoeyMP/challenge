package com.joey.tenpo.challenge.service;

import com.joey.tenpo.challenge.config.CacheName;
import com.joey.tenpo.challenge.exception.ExternalServiceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CachingService {

    private final CacheManager cacheManager;
    private final ExternalService externalService;

    @CachePut(value = CacheName.EXTERNAL_PERCENTAGE, key = "#amount")
    @Retryable(retryFor = {Exception.class}, maxAttempts = 3, backoff = @Backoff(2000), recover = "getDataFromCache")
    public BigDecimal getPercentage(int amount) throws Exception {
        log.info("Calling CachingService.getPercentage with amount {}", amount);
        return externalService.getPercentage();
    }

    @Recover
    private BigDecimal getDataFromCache(Exception exception, int key) throws Exception {
        log.info("Calling recover method");
        CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache(CacheName.EXTERNAL_PERCENTAGE);
        return Optional.ofNullable(caffeineCache)
                .map(c -> c.get(key, BigDecimal.class))
                .orElseThrow(() -> new ExternalServiceException(exception.getMessage()));
    }

    @Cacheable(value = CacheName.EXTERNAL_PERCENTAGE, key = "#amount")
    public BigDecimal getPercentageCacheable(int amount) throws Exception {
        return externalService.getPercentage();
    }


}

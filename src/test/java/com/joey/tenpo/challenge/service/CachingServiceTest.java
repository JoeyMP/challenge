package com.joey.tenpo.challenge.service;

import com.joey.tenpo.challenge.exception.ExternalServiceException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest
public class CachingServiceTest {

    @MockitoSpyBean
    private CacheManager cacheManager;

    @MockitoBean
    private ExternalService externalService;

    @Autowired
    private CachingService cachingService;

    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("challenge-db")
            .withUsername("sa")
            .withPassword("sa");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }


    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    @Test
    void getPercentage() throws Exception {
        cachingService.getPercentage(1);
        cachingService.getPercentage(1);
        cachingService.getPercentage(1);

        verify(externalService, times(3)).getPercentage();
    }

    @Test
    void getPercentage_Fail_And_Retry_Then_Get_Cache_Value() throws Exception {
        Mockito.when(externalService.getPercentage()).thenReturn(BigDecimal.TWO).thenThrow(Exception.class);

        //set first cache value
        cachingService.getPercentage(1);
        BigDecimal result = cachingService.getPercentage(1);

        assertEquals(BigDecimal.TWO, result);
        verify(externalService, times(4)).getPercentage();
    }

    @Test
    void getPercentage_Fail_And_Retry_Then_Empty_Cache_Value() throws Exception {
        Mockito.when(externalService.getPercentage()).thenThrow(Exception.class);

        Assertions.assertThrows(ExternalServiceException.class, () -> {
            cachingService.getPercentage(1);
        });

        verify(externalService, times(3)).getPercentage();
    }

    @Test
    void getPercentageCacheable() throws Exception {
        cachingService.getPercentageCacheable(5);
        cachingService.getPercentageCacheable(5);
        cachingService.getPercentageCacheable(5);
        cachingService.getPercentageCacheable(6);

        verify(externalService, times(2)).getPercentage();
    }

}
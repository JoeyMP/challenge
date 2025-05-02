package com.joey.tenpo.challenge.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@AllArgsConstructor
public class ExternalServiceImpl implements ExternalService {

    @Override
    public BigDecimal getPercentage() throws Exception {
        log.info("Calling external service");
        extracted(1000);
        return new BigDecimal(50);
    }

    private static void extracted(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

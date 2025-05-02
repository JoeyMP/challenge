package com.joey.tenpo.challenge.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@AllArgsConstructor
public class ComputeServiceImpl implements ComputeService {

    private final CachingService cachingService;

    @Override
    public BigDecimal compute(int num1, int num2) throws Exception {
        int sum = num1 + num2;
        BigDecimal percentage = cachingService.getPercentage(sum).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);
        BigDecimal result = BigDecimal.valueOf(sum)
                .add(BigDecimal.valueOf(sum).multiply(percentage));
        return result.setScale(2, RoundingMode.HALF_EVEN);
    }
}
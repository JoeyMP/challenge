package com.joey.tenpo.challenge.service;

import com.joey.tenpo.challenge.exception.ExternalServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ComputeServiceImplTest {

    @Mock
    private CachingService cachingService;

    @InjectMocks
    private ComputeServiceImpl computeService;

    @Test
    public void compute() throws Exception {
        when(cachingService.getPercentage(anyInt())).thenReturn(BigDecimal.TEN);
        BigDecimal result = computeService.compute(50, 15);

        assertEquals(BigDecimal.valueOf(71.5).setScale(2, RoundingMode.HALF_EVEN), result);
    }

    @Test
    public void compute_CachingServiceThrowException() throws Exception {
        when(cachingService.getPercentage(anyInt())).thenThrow(ExternalServiceException.class);
        Assertions.assertThrows(ExternalServiceException.class, () -> {
            computeService.compute(50, 15);
        });
    }
}
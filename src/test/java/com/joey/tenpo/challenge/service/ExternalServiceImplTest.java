package com.joey.tenpo.challenge.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExternalServiceImplTest {

    @InjectMocks
    private ExternalServiceImpl externalService;

    @Test
    public void getPercentage() throws Exception {
        externalService.getPercentage();
    }
}
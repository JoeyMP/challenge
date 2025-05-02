package com.joey.tenpo.challenge.controller;

import com.joey.tenpo.challenge.dto.HistoryDTO;
import com.joey.tenpo.challenge.exception.ExternalServiceException;
import com.joey.tenpo.challenge.service.ComputeService;
import com.joey.tenpo.challenge.service.HistoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ChallengeController.class)
@EnableSpringDataWebSupport(
        pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO
)
public class ChallengeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private HistoryService historyService;

    @MockitoBean
    private ComputeService computeService;

    @Test
    public void compute() throws Exception {
        when(computeService.compute(eq(20), eq(10))).thenReturn(BigDecimal.valueOf(30));
        this.mvc.perform(get("/api/compute/20/10")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void compute_ThrowExternalServiceException() throws Exception {
        when(computeService.compute(eq(20), eq(10))).thenThrow(new ExternalServiceException("external exception"));
        this.mvc.perform(get("/api/compute/20/10")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("external exception"))
                .andExpect(jsonPath("$.description").value("External Service Fail"));
    }

    @Test
    public void compute_ThrowAnyException() throws Exception {
        when(computeService.compute(eq(20), eq(10))).thenThrow(new NullPointerException("any exception"));
        this.mvc.perform(get("/api/compute/20/10")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.detail").value("Internal server error"))
                .andExpect(jsonPath("$.description").value("Unknown internal server error"));
    }

    @Test
    public void filterHistory() throws Exception {
        Pageable pageableMock = Mockito.mock(Pageable.class);
        List<HistoryDTO> content = Arrays.asList(
                new HistoryDTO(1L, LocalDateTime.now(), "/api/compute/5/6", "{Headers=, Body=}", "{Response=200, Headers=, Body=10}"),
                new HistoryDTO(2L, LocalDateTime.now(), "/api/compute/13/4", "{Headers=, Body=}", "{Response=200, Headers=, Body=20}")
        );
        Page<HistoryDTO> pageHistoryDTO = new PageImpl<>(content, pageableMock, content.size());

        when(historyService.filterHistory(any(Pageable.class))).thenReturn(pageHistoryDTO);

        this.mvc.perform(get("/api/history/filter?size=20&page=0")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].id").value("1"))
                .andExpect(jsonPath("$.content.[1].id").value("2"));
    }

    @Test
    public void filterHistory_ThrowAnyException() throws Exception {
        when(historyService.filterHistory(any(Pageable.class))).thenThrow(new NullPointerException("any exception"));

        this.mvc.perform(get("/api/history/filter?size=20&page=0")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.detail").value("Internal server error"))
                .andExpect(jsonPath("$.description").value("Unknown internal server error"));
    }
}
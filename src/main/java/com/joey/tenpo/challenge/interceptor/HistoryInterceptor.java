package com.joey.tenpo.challenge.interceptor;

import com.joey.tenpo.challenge.dto.HistoryDTO;
import com.joey.tenpo.challenge.service.HistoryService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
public class HistoryInterceptor extends OncePerRequestFilter {

    private final HistoryService historyService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestBody = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        // Create a custom response wrapper to capture the response body
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        // Proceed with the filter chain
        filterChain.doFilter(request, responseWrapper);

        byte[] responseArray = responseWrapper.getContentAsByteArray();
        String responseBody = new String(responseArray, StandardCharsets.UTF_8);

        saveHistory(request, requestBody, responseWrapper, responseBody);

        // Copy the content of the wrapper to the actual response
        responseWrapper.copyBodyToResponse();
    }

    public void saveHistory(HttpServletRequest request, String requestBody,
                             ContentCachingResponseWrapper response, String responseBody) {
        String endpoint = request.getMethod() + " " + request.getRequestURI();

        if (!request.getRequestURI().contains("/api/compute")){
            return;
        }
        Map<String, Object> requestDetail = new HashMap<>();
        requestDetail.put("Headers", getHeaders(request));
        requestDetail.put("Body", requestBody);

        Map<String, Object> responseDetail = new HashMap<>();
        responseDetail.put("Response", response.getStatus());
        responseDetail.put("Headers", getHeaders(response));
        responseDetail.put("Body", responseBody);

        historyService.save(new HistoryDTO(null, LocalDateTime.now(), endpoint,
                requestDetail.toString(), responseDetail.toString()));
    }

    private String getHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder headers = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.append(headerName).append(": ").append(request.getHeader(headerName)).append(", ");
        }
        return headers.toString();
    }

    private String getHeaders(ContentCachingResponseWrapper response) {
        Collection<String> headerNames = response.getHeaderNames();
        StringBuilder headers = new StringBuilder();
        for (String headerName : headerNames) {
            headers.append(headerName).append(": ").append(response.getHeader(headerName)).append(", ");
        }
        return headers.toString();
    }
}

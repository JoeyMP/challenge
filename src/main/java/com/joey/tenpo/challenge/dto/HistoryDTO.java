package com.joey.tenpo.challenge.dto;

import java.time.LocalDateTime;

public record HistoryDTO(Long id, LocalDateTime creationDate, String endpoint, String request, String response) {
}

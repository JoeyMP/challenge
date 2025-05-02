package com.joey.tenpo.challenge.service;

import com.joey.tenpo.challenge.dto.HistoryDTO;
import com.joey.tenpo.challenge.entity.HistoryDAO;
import com.joey.tenpo.challenge.repository.HistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HistoryServiceImplTest {

    @Mock
    private HistoryRepository historyRepository;

    @InjectMocks
    private HistoryServiceImpl historyService;

    @Test
    public void save() {
        HistoryDTO historyDTO = new HistoryDTO(null, LocalDateTime.now(), "/api/5/6", "request", "response");
        when(historyRepository.save(any(HistoryDAO.class))).thenReturn(HistoryDAO.builder().build());

        historyService.save(historyDTO);
        verify(historyRepository).save(any(HistoryDAO.class));
    }

    @Test
    public void filterHistory() {
        Pageable pageableMock = Mockito.mock(Pageable.class);
        List<HistoryDAO> content = Arrays.asList(HistoryDAO.builder().build(), HistoryDAO.builder().build());
        Page<HistoryDAO> pageHistoryDAO = new PageImpl<>(content, pageableMock, content.size());
        when(historyRepository.findAll(any(Pageable.class))).thenReturn(pageHistoryDAO);

        Pageable pageable = PageRequest.of(0, 2, Sort.by("creationDate"));
        Page<HistoryDTO> result = historyService.filterHistory(pageable);

        assertEquals(2, result.getTotalElements());
        verify(historyRepository).findAll(any(Pageable.class));
    }
}
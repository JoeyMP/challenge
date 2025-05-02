package com.joey.tenpo.challenge.service;

import com.joey.tenpo.challenge.dto.HistoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface HistoryService {

    Page<HistoryDTO> filterHistory(Pageable pageable);

    void save(HistoryDTO dto);
}

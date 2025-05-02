package com.joey.tenpo.challenge.service;

import com.joey.tenpo.challenge.dto.HistoryDTO;
import com.joey.tenpo.challenge.entity.HistoryDAO;
import com.joey.tenpo.challenge.mapper.HistoryMapper;
import com.joey.tenpo.challenge.repository.HistoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;

    @Override
    @Async("asyncTaskExecutor")
    public void save(HistoryDTO dto) {
        log.info("start saving history{}", dto.toString());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        historyRepository.save(HistoryMapper.of(dto));
        log.info("end saving history");
    }

    @Override
    public Page<HistoryDTO> filterHistory(Pageable pageable) {
        final Page<HistoryDAO> page = historyRepository.findAll(pageable);

        return new PageImpl<>(page.getContent()
                .stream()
                .map(HistoryMapper::of)
                .collect(Collectors.toList()),
                pageable, page.getTotalElements());
    }
}

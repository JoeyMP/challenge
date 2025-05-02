package com.joey.tenpo.challenge.mapper;

import com.joey.tenpo.challenge.dto.HistoryDTO;
import com.joey.tenpo.challenge.entity.HistoryDAO;

public class HistoryMapper {
    public static HistoryDTO of(HistoryDAO dao) {
        return new HistoryDTO(dao.getId(), dao.getCreationDate(), dao.getEndpoint(), dao.getRequest(), dao.getResponse());
    }

    public static HistoryDAO of(HistoryDTO dto) {
        return HistoryDAO.builder()
                .creationDate(dto.creationDate())
                .endpoint(dto.endpoint())
                .request(dto.request())
                .response(dto.response())
                .build();
    }

}

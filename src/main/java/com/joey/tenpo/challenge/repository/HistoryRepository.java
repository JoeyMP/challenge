package com.joey.tenpo.challenge.repository;

import com.joey.tenpo.challenge.entity.HistoryDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryDAO, Long> {
}

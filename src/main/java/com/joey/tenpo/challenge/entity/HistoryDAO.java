package com.joey.tenpo.challenge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "history")
@Entity
public class HistoryDAO {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "request")
    private String request;

    @Column(name = "response")
    private String response;

}

package br.univates.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "api_logs")
public class api_log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String endpoint;

    @Column
    private String method;

    @Column(name = "status_code")
    private int status;

    @Column(name = "request_body")
    private String request;

    @Column(name = "response_body")
    private String response;

    @Column
    private long response_time;
}

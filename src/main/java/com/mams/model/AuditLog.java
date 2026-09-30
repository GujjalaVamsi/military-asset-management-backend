package com.mams.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity @Table(name = "audit_logs") @Getter @Setter
public class AuditLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String username;
    private String method;
    @Column(length = 500) private String path;
    private int status;
    private LocalDateTime createdAt = LocalDateTime.now();
}

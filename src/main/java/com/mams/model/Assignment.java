package com.mams.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity @Table(name = "assignments") @Getter @Setter
public class Assignment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) private Base base;
    @ManyToOne(optional = false) private EquipmentType equipment;
    private String personnel;
    private int quantity;
    @Enumerated(EnumType.STRING) private Kind kind;
    private LocalDate eventDate;
    private String createdBy;
    private LocalDateTime createdAt = LocalDateTime.now();
}

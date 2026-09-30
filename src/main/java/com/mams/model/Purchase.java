package com.mams.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity @Table(name = "purchases") @Getter @Setter
public class Purchase {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) private Base base;
    @ManyToOne(optional = false) private EquipmentType equipment;
    private int quantity;
    private LocalDate eventDate;
    private String createdBy;
    private LocalDateTime createdAt = LocalDateTime.now();
}

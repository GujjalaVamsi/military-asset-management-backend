package com.mams.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity @Table(name = "transfers") @Getter @Setter
public class Transfer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) private Base fromBase;
    @ManyToOne(optional = false) private Base toBase;
    @ManyToOne(optional = false) private EquipmentType equipment;
    private int quantity;
    private LocalDate eventDate;
    private String createdBy;
    private LocalDateTime createdAt = LocalDateTime.now();
}

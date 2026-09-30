package com.mams.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity @Table(name = "equipment_types") @Getter @Setter
public class EquipmentType {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(unique = true, nullable = false) private String name;
    private String category; // VEHICLE, WEAPON, AMMUNITION
}

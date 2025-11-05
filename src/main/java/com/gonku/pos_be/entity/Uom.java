package com.gonku.pos_be.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "uom")
public class Uom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String code; // e.g. "PCS", "KG", "LTR"

    @Column(nullable = false, length = 50)
    private String name; // e.g. "Pieces", "Kilogram", "Liter"

    @Column
    private String description;

    @Column(length = 10)
    private String symbol; // e.g. "pcs", "kg", "L"

    @Column(nullable = false)
    private Boolean isActive;

    @PrePersist
    public void prePersist() {
        if (isActive == null) {
            isActive = true;
        }
    }
}


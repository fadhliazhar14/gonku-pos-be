package com.gonku.pos_be.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock_mutation")
public class StockMutation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type")
    private StockReferenceType stockReferenceType;

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "quantity", precision = 18, scale = 3)
    private BigDecimal quantity;
}
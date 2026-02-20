package com.gonku.pos_be.entity.product;

import com.gonku.pos_be.entity.category.Category;
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
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 15)
    private String barcode;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @Column(name = "buying_price", nullable = false)
    private BigDecimal buyingPrice;

    @Column(name = "sale_price", nullable = false)
    private BigDecimal salePrice;

    @Column(precision = 18, scale = 3)
    private BigDecimal stock;

    @Column
    private String picturePath;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom_id")
    private Uom uom;

    @PrePersist
    public void prePersist() {
        if (isActive == null) {
            isActive = true;
        }
    }
}

package com.gonku.pos_be.repository;

import com.gonku.pos_be.entity.Category;
import com.gonku.pos_be.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Boolean existsByBarcode(String barcode);
    Boolean existsByBarcodeAndIdNot(String barcode, Long id);

    @Query("""
    SELECT p
    FROM Product p
    WHERE 
        (
            :search IS NULL OR :search = ''
            OR p.barcode LIKE CONCAT('%', :search, '%')
            OR p.name LIKE CONCAT('%', :search, '%')
            OR p.description LIKE CONCAT('%', :search, '%')
        )
        AND (p.isActive = :isActive OR p.isActive IS NULL)
    """)
    Page<Product> findAllWithPagination(@Param("search") String search, @Param("isActive") Boolean isActive, Pageable pageable);
}

package com.gonku.pos_be.repository;

import com.gonku.pos_be.entity.product.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Boolean existsByBarcode(String barcode);
    Boolean existsByBarcodeAndIdNot(String barcode, Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    List<Product> findAllByIdInForUpdate(@Param("ids") List<Long> ids);

    @Query("""
    SELECT p
    FROM Product p
    WHERE 
        (
            :search IS NULL OR :search = ''
            OR LOWER(p.barcode) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))
        )
        AND (p.isActive = :isActive OR p.isActive IS NULL)
    """)
    Page<Product> findAllWithPagination(@Param("search") String search, @Param("isActive") Boolean isActive, Pageable pageable);
}

package com.gonku.pos_be.repository;

import com.gonku.pos_be.entity.Uom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UomRepository extends JpaRepository<Uom, Long> {
    Boolean existsByName(String name);
    Boolean existsByNameAndIdNot(String name, Long id);

    @Query("""
    SELECT u
    FROM Uom u
    WHERE 
        (
            :search IS NULL OR :search = ''
            OR u.code LIKE CONCAT('%', :search, '%')
            OR u.name LIKE CONCAT('%', :search, '%')
            OR u.description LIKE CONCAT('%', :search, '%')
            OR u.symbol LIKE CONCAT('%', :search, '%')
        )
        AND (u.isActive = :isActive OR u.isActive IS NULL)
    """)
    Page<Uom> findAllWithPagination(@Param("search") String search, @Param("isActive") Boolean isActive, Pageable pageable);
}

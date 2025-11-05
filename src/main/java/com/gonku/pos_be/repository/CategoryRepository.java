package com.gonku.pos_be.repository;

import com.gonku.pos_be.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Boolean existsByName(String name);
    Boolean existsByNameAndIdNot(String name, Long id);

    @Query("""
    SELECT c
    FROM Category c
    WHERE 
        (
            :search IS NULL OR :search = ''
            OR c.name LIKE CONCAT('%', :search, '%')
            OR c.description LIKE CONCAT('%', :search, '%')
        )
        AND (c.isActive = :isActive OR c.isActive IS NULL)
    """)
    Page<Category> findAllWithPagination(@Param("search") String search, @Param("isActive") Boolean isActive, Pageable pageable);
}

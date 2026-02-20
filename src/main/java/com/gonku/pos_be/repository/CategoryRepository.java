package com.gonku.pos_be.repository;

import com.gonku.pos_be.entity.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Boolean existsByName(String name);
    Boolean existsByNameAndIdNot(String name, Long id);

    @Query("""
    SELECT c
    FROM Category c
    WHERE 
        (
            :search IS NULL OR :search = ''
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%'))
        )
        AND (c.isActive = :isActive OR c.isActive IS NULL)
    """)
    Page<Category> findAllWithPagination(@Param("search") String search, @Param("isActive") Boolean isActive, Pageable pageable);
}

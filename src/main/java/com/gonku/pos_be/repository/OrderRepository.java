package com.gonku.pos_be.repository;

import com.gonku.pos_be.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
    SELECT o
    FROM Order o
    WHERE 
        (
            :search IS NULL OR :search = ''
            OR o.receiptNumber LIKE CONCAT('%', :search, '%')
            OR o.outletName LIKE CONCAT('%', :search, '%')
            OR o.tableNumber LIKE CONCAT('%', :search, '%')
        )
    """)
    Page<Order> findAllWithPagination(@Param("search") String search, Pageable pageable);
}

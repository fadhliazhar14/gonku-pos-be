package com.gonku.pos_be.repository;

import com.gonku.pos_be.entity.order.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderPaymentRepository extends JpaRepository<OrderPayment, Long> {
    List<OrderPayment> findByOrderId(Long orderId);
}

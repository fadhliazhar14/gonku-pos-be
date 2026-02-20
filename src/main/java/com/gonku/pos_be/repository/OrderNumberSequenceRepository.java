package com.gonku.pos_be.repository;

import com.gonku.pos_be.entity.order.OrderNumberSequence;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface OrderNumberSequenceRepository extends JpaRepository<OrderNumberSequence, LocalDate> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM OrderNumberSequence s WHERE s.dateKey = :date")
    Optional<OrderNumberSequence> findForUpdate(@Param("date") LocalDate date);
}


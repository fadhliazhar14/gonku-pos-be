package com.gonku.pos_be.repository;

import com.gonku.pos_be.entity.StockMutation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockMutationRepository extends JpaRepository<StockMutation, Long> {

}

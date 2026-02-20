package com.gonku.pos_be.repository;

import com.gonku.pos_be.entity.common.Outlet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutletRepository extends JpaRepository<Outlet, Long> {

}

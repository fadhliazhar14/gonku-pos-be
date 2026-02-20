package com.gonku.pos_be.repository;

import com.gonku.pos_be.entity.common.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

}

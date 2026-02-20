package com.gonku.pos_be.repository;

import com.gonku.pos_be.entity.common.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppConfigRepository extends JpaRepository<AppConfig, Long> {
    Optional<AppConfig> findByKey(String key);
}


package com.gonku.pos_be.service;

import com.gonku.pos_be.entity.common.AppConfig;
import com.gonku.pos_be.repository.AppConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppConfigService {
    private final AppConfigRepository repository;

    @Value("${tax.rate:0.11}")
    private BigDecimal defaultTaxRate;

    @Cacheable("appConfigs")
    public Optional<String> getConfigValue(String key) {
        return repository.findByKey(key).map(AppConfig::getValue);
    }

    public BigDecimal getTaxRate() {
        return getConfigValue("tax.rate")
                .map(BigDecimal::new)
                .orElse(defaultTaxRate);
    }

    @CacheEvict(value = "appConfigs", allEntries = true)
    public void updateConfig(String key, String value, String description) {
        AppConfig config = repository.findByKey(key)
                .orElseGet(AppConfig::new);
        config.setKey(key);
        config.setValue(value);
        config.setDescription(description);
        repository.save(config);
    }

    @CacheEvict(value = "appConfigs", allEntries = true)
    public void clearCache() {
        // nothing, annotation will clear cache
    }
}

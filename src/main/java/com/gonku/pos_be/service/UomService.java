package com.gonku.pos_be.service;

import com.gonku.pos_be.dto.uom.UomMapper;
import com.gonku.pos_be.repository.UomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UomService {
    private final UomRepository uomRepository;
}

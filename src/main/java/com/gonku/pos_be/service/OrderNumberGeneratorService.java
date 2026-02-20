package com.gonku.pos_be.service;

import com.gonku.pos_be.entity.order.OrderNumberSequence;
import com.gonku.pos_be.repository.OrderNumberSequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class OrderNumberGeneratorService {
    private final OrderNumberSequenceRepository orderNumberSequenceRepository;

    @Transactional
    public String generateOrderNumber() {
        LocalDate today = LocalDate.now();

        // Ambil atau buat entri baru untuk hari ini
        OrderNumberSequence seq = orderNumberSequenceRepository.findForUpdate(today)
                .orElseGet(() -> {
                    OrderNumberSequence newSeq = new OrderNumberSequence();
                    newSeq.setDateKey(today);
                    newSeq.setLastNumber(0L);

                    return newSeq;
                });

        long nextNumber = seq.getLastNumber() + 1;
        seq.setLastNumber(nextNumber);
        orderNumberSequenceRepository.save(seq);

        String formattedNumber = String.format("%06d", nextNumber);

        return String.format("ORD-%s-%s",
                today.format(DateTimeFormatter.BASIC_ISO_DATE),
                formattedNumber);
    }
}


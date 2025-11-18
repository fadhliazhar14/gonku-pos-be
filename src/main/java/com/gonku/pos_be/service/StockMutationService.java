package com.gonku.pos_be.service;

import com.gonku.pos_be.entity.Order;
import com.gonku.pos_be.entity.Product;
import com.gonku.pos_be.entity.StockMutation;
import com.gonku.pos_be.entity.StockReferenceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StockMutationService {

    public List<StockMutation> buildReturnStockMutations(
            List<StockMutation> lastMutations,
            Order order
    ) {
        return lastMutations.stream()
                .map(sm -> new StockMutation(
                        null,
                        sm.getProduct(),
                        StockReferenceType.RETURN,
                        order.getReceiptNumber(),
                        sm.getQuantity().abs()
                ))
                .toList();
    }

    public void applyReturnedStockMutation(
            List<StockMutation> returnMutations,
            Map<Long, Product> productMap
    ) {
        returnMutations.forEach(sm -> {
            Product p = productMap.get(sm.getProduct().getId());
            p.setStock(p.getStock().add(sm.getQuantity()));
        });
    }
}

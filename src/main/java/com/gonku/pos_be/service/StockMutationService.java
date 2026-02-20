package com.gonku.pos_be.service;

import com.gonku.pos_be.entity.common.Outlet;
import com.gonku.pos_be.entity.common.Tenant;
import com.gonku.pos_be.entity.order.Order;
import com.gonku.pos_be.entity.product.Product;
import com.gonku.pos_be.entity.inventory.StockMutation;
import com.gonku.pos_be.entity.inventory.StockReferenceType;
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
                        sm.getQuantity().abs(),
                        new Tenant(),
                        new Outlet()
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

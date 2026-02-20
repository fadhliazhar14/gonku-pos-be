package com.gonku.pos_be.entity.order;

import com.gonku.pos_be.entity.common.Outlet;
import com.gonku.pos_be.entity.common.Tenant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_number_sequence")
public class OrderNumberSequence {
    @Id
    private LocalDate dateKey;

    private long lastNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outlet_id")
    private Outlet outlet;
}


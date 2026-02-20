package com.gonku.pos_be.service;

import com.gonku.pos_be.constant.ResponseMessages;
import com.gonku.pos_be.dto.common.PageRequestDto;
import com.gonku.pos_be.dto.common.PageResponseDto;
import com.gonku.pos_be.dto.order.*;
import com.gonku.pos_be.entity.inventory.StockMutation;
import com.gonku.pos_be.entity.inventory.StockReferenceType;
import com.gonku.pos_be.entity.order.Order;
import com.gonku.pos_be.entity.order.OrderItem;
import com.gonku.pos_be.entity.order.OrderPayment;
import com.gonku.pos_be.entity.order.OrderStatus;
import com.gonku.pos_be.entity.payment.PaymentStatus;
import com.gonku.pos_be.entity.product.Product;
import com.gonku.pos_be.exception.BusinessValidationException;
import com.gonku.pos_be.exception.ResourceNotFoundException;
import com.gonku.pos_be.repository.*;
import com.gonku.pos_be.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderValidationService orderValidationService;
    private final OrderNumberGeneratorService orderNumberGeneratorService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderPaymentRepository orderPaymentRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderPaymentMapper orderPaymentMapper;
    private final StockMutationRepository stockMutationRepository;
    private final StockMutationService stockMutationService;
    private final ProductRepository productRepository;
    private static final String ENTITY_NOT_FOUND = "Order";
    private static final BigDecimal TAX_RATE = new BigDecimal("0.11");

    public PageResponseDto<OrderResponseDto> findAll(PageRequestDto pageRequestDto) {
        Pageable pageable = PageUtil.createPageable(pageRequestDto);
        Page<Order> orderPage = orderRepository.findAllWithPagination(
                pageRequestDto.getSearch(),
                pageable);
        List<OrderResponseDto> responseDtoList = orderPage.stream()
                .map(orderMapper::toDto)
                .toList();

        return PageUtil.createPageResponse(responseDtoList, pageable, orderPage.getTotalElements());
    }

    public OrderDetailsResponseDto findById(Long id) {
        OrderResponseDto order =  orderMapper.toDto(orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessages.notFound(ENTITY_NOT_FOUND))));

        List<OrderItemResponseDto> orderItems = orderItemMapper.toDtoList(orderItemRepository.findByOrderId(id));
        List<OrderPaymentResponseDto> orderPayments = orderPaymentMapper.toDtoList(orderPaymentRepository.findByOrderId(id));

        return new OrderDetailsResponseDto(order, orderItems, orderPayments);
    }

    @Transactional
    public OrderResponseDto add(OrderRequestDto requestDto) {
        List<Product> products = productRepository.findAllByIdInForUpdate(
                requestDto.getItems().stream()
                        .map(OrderItemRequestDto::getProductId)
                        .toList()
        );

        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        // Check inactive products
        List<Product> inactiveItems = products.stream().filter(p -> !p.getIsActive()).toList();

        if (!inactiveItems.isEmpty()) {
            throw new BusinessValidationException("Order can not be processed. There are any inactive items for current order");
        }

        // Set order header metadata
        String receiptNumber = orderNumberGeneratorService.generateOrderNumber();
        Order order = orderMapper.toEntity(requestDto);
        order.setReceiptNumber(receiptNumber);
        order.setStatus(OrderStatus.UNPAID);
        order.setOrderDate(LocalDate.now());

        // Mapping order details
        List<OrderItem> items = mapItems(order, requestDto.getItems(), productMap);
        List<OrderPayment> payments = mapPayments(order, requestDto.getPayments());
        List<StockMutation> mutations = mapStockMutations(order.getReceiptNumber(), requestDto.getItems(), productMap);

        // Order amounts equality validation
        BigDecimal totalAmountItems = calculateTotalAmountItems(items);
        BigDecimal totalAmountPayments = calculateTotalPayments(payments);

        // Set order amounts
        BigDecimal discountTotal = order.getDiscountTotal().add(calculateTotalDiscountItems(items));
        BigDecimal taxBase = totalAmountItems.subtract(discountTotal);
        BigDecimal taxTotal = taxBase.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);

        order.setDiscountTotal(discountTotal);
        order.setSubtotal(totalAmountItems);
        order.setTaxTotal(taxTotal);
        order.setPaidAmount(totalAmountPayments);
        order.setTotalAmount(totalAmountItems.subtract(discountTotal).add(taxTotal));
        order.setChangeAmount(order.getPaidAmount().subtract(order.getTotalAmount()));

        boolean isAmountValid = order.getSubtotal().compareTo(totalAmountItems) == 0
                && order.getSubtotal().compareTo(totalAmountPayments) <= 0;

        if (!isAmountValid) {
            throw new BusinessValidationException(
                    "Order total mismatch between order, items, and payments."
            );
        }

        // Save all entities in one transaction
        Order savedOrder = orderRepository.save(order);
        orderItemRepository.saveAll(items);
        orderPaymentRepository.saveAll(payments);
        stockMutationRepository.saveAll(mutations);
        productRepository.saveAll(products);

        return orderMapper.toDto(savedOrder);
    }

    @Transactional
    public OrderStatus cancelOrVoid(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessages.notFound(ENTITY_NOT_FOUND)));

        // Check current order status
        OrderStatus status = order.getStatus();
        orderValidationService.validateCancelable(status);

        // Change order status
        OrderStatus newStatus = determineNewStatus(order);
        order.setStatus(newStatus);

        // Returned stock mutation
        List<StockMutation> lastStockMutation = stockMutationRepository.findByReferenceId(order.getReceiptNumber());
        List<StockMutation> returnedStockMutation = stockMutationService.buildReturnStockMutations(lastStockMutation, order);

        // Update product stock
        List<Long> productIds = lastStockMutation.stream()
                .map(sm -> sm.getProduct().getId())
                .toList();

        List<Product> products = productRepository.findAllByIdInForUpdate(productIds);

        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        stockMutationService.applyReturnedStockMutation(returnedStockMutation, productMap);

        // Save all entities
        orderRepository.save(order);
        productRepository.saveAll(productMap.values());
        stockMutationRepository.saveAll(returnedStockMutation);

        return newStatus;
    }

    private OrderStatus determineNewStatus(Order order) {
        OrderStatus status = order.getStatus();

        return (status == OrderStatus.PAID || status == OrderStatus.COMPLETED)
                ? OrderStatus.VOIDED
                : OrderStatus.CANCELLED;
    }

    private List<OrderItem> mapItems(Order order, List<OrderItemRequestDto> items, Map<Long, Product> productMap) {
        if (items.isEmpty()) return Collections.emptyList();

        List<OrderItem> mappedOrderItems = new ArrayList<>();

        for (OrderItemRequestDto item : items) {
            Product product = productMap.get(item.getProductId());
            if (product == null)
                throw new ResourceNotFoundException("Product not found: " + item.getProductId());

            BigDecimal quantity = item.getQuantity();
            BigDecimal amount = product.getSalePrice()
                    .multiply(quantity)
                    .subtract(item.getDiscount() == null ? BigDecimal.ZERO : item.getDiscount());

            OrderItem currentItem = orderItemMapper.toEntity(item);
            currentItem.setOrder(order);
            currentItem.setProductId(item.getProductId());
            currentItem.setProductName(product.getName());
            currentItem.setPrice(product.getSalePrice());
            currentItem.setAmount(amount);

            mappedOrderItems.add(currentItem);
        }

        return mappedOrderItems;
    }

    private List<StockMutation> mapStockMutations(String orderReceiptNo, List<OrderItemRequestDto> items, Map<Long, Product> productMap) {
        if (items.isEmpty()) return Collections.emptyList();

        List<StockMutation> mappedMutations = new ArrayList<>();

        for (OrderItemRequestDto item : items) {
            Product product = productMap.get(item.getProductId());
            if (product == null)
                throw new ResourceNotFoundException("Product not found: " + item.getProductId());

            if (product.getStock().compareTo(item.getQuantity()) <= 0)
                throw new BusinessValidationException("Insufficient stock for " + product.getName());

            product.setStock(product.getStock().subtract(item.getQuantity()));

            StockMutation mutation = new StockMutation();
            mutation.setProduct(product);
            mutation.setQuantity(item.getQuantity().negate());
            mutation.setStockReferenceType(StockReferenceType.SALE);
            mutation.setReferenceId(orderReceiptNo);
            mappedMutations.add(mutation);
        }

        return mappedMutations;
    }

    private List<OrderPayment> mapPayments(Order order, List<OrderPaymentRequestDto> payments) {
        if (payments.isEmpty()) return Collections.emptyList();

        List<OrderPayment> mappedPayments = new ArrayList<>();

        for (OrderPaymentRequestDto payment : payments) {
            OrderPayment currentPayment = orderPaymentMapper.toEntity(payment);
            currentPayment.setOrder(order);
            currentPayment.setStatus(PaymentStatus.PENDING);

            mappedPayments.add(currentPayment);
        }

        return mappedPayments;
    }

    private BigDecimal calculateTotalAmountItems(List<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalDiscountItems(List<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalPayments(List<OrderPayment> payments) {
        return payments.stream()
                .map(OrderPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

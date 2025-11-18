package com.gonku.pos_be.controller;

import com.gonku.pos_be.constant.ResponseMessages;
import com.gonku.pos_be.dto.common.PageRequestDto;
import com.gonku.pos_be.dto.common.PageResponseDto;
import com.gonku.pos_be.dto.order.OrderRequestDto;
import com.gonku.pos_be.dto.order.OrderResponseDto;
import com.gonku.pos_be.entity.OrderStatus;
import com.gonku.pos_be.service.OrderService;
import com.gonku.pos_be.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;
    private static final String ENTITY_RESPONSE = "Order";
    private static final String PATH_WITH_ID = "/orders/{id}";
    private static final String PATH_WITHOUT_ID = "/orders";

    @GetMapping(PATH_WITHOUT_ID)
    public ResponseEntity<ApiResponse<PageResponseDto<OrderResponseDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "true") Boolean isActive,
            @RequestParam(required = false) String search
    ) {
        PageRequestDto pageRequestDto = new PageRequestDto(page, size, sort, direction, isActive, search);
        PageResponseDto<OrderResponseDto> orders = orderService.findAll(pageRequestDto);
        ApiResponse<PageResponseDto<OrderResponseDto>> response = ApiResponse.success(ResponseMessages.SUCCESS, orders);

        return ResponseEntity.ok(response);
    }

    @GetMapping(PATH_WITH_ID)
    public ResponseEntity<ApiResponse<OrderResponseDto>> getById(@PathVariable Long id) {
        OrderResponseDto order = orderService.findById(id);
        ApiResponse<OrderResponseDto> response = ApiResponse.success(ResponseMessages.SUCCESS, order);

        return ResponseEntity.ok(response);
    }

    @PostMapping(PATH_WITHOUT_ID)
    public ResponseEntity<ApiResponse<OrderResponseDto>> create(@Valid @RequestBody OrderRequestDto requestDto) {
        OrderResponseDto createdOrder = orderService.add(requestDto);
        ApiResponse<OrderResponseDto> response = ApiResponse.success(
                HttpStatus.CREATED.value(), ResponseMessages.created(ENTITY_RESPONSE), createdOrder);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdOrder.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PostMapping(PATH_WITH_ID + "/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelOrVoid(@PathVariable Long id) {
        OrderStatus newStatus = orderService.cancelOrVoid(id);
        String message = newStatus.equals(OrderStatus.CANCELLED)
                ? ResponseMessages.cancelled("Order")
                : ResponseMessages.voided("Order");

        ApiResponse<Void> response = ApiResponse.success(message, null);

        return ResponseEntity.ok(response);
    }
}

package com.forsaken.ecommerce.order.order.controller;

import com.forsaken.ecommerce.common.responses.ApiResponse;
import com.forsaken.ecommerce.order.order.dto.OrderRequest;
import com.forsaken.ecommerce.order.order.dto.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * REST controller interface for managing Orders within the system.
 * <p>
 * Provides endpoints for creating orders, retrieving all orders,
 * fetching orders for a specific customer, and retrieving an order by its ID.
 * </p>
 */
@RequestMapping("/api/v1/orders")
public interface IOrderController {

    /**
     * Creates a new order.
     *
     * @param request the order creation payload, validated before processing
     * @return {@link ResponseEntity} containing the created order ID wrapped in {@link ApiResponse}
     * @throws ExecutionException   if async processing of order creation fails
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    @PostMapping
    ResponseEntity<ApiResponse<Integer>> createOrder(
            @RequestBody @Valid final OrderRequest request
    ) throws ExecutionException, InterruptedException;

    /**
     * Retrieves all orders stored in the system.
     *
     * @return {@link ResponseEntity} containing a list of {@link OrderResponse}
     * wrapped in {@link ApiResponse}
     */
    @GetMapping
    ResponseEntity<ApiResponse<List<OrderResponse>>> findAll();

    /**
     * Retrieves all orders associated with a specific customer, optionally
     * filtered by a date range.
     *
     * @param customerId the unique ID of the customer whose orders are being retrieved
     * @param fromDate   optional filter for the starting date/time (inclusive)
     * @param toDate     optional filter for the ending date/time (inclusive)
     * @return {@link ResponseEntity} containing a list of matching {@link OrderResponse}
     * wrapped in {@link ApiResponse}
     */
    @GetMapping("/{customerId}")
    ResponseEntity<ApiResponse<List<OrderResponse>>> findAllOrdersByCustomerId(
            @PathVariable("customerId") final String customerId,

            @RequestParam(value = "fromDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime fromDate,

            @RequestParam(value = "toDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime toDate
    );

    /**
     * Retrieves a single order by its unique identifier.
     *
     * @param orderId the ID of the order to retrieve
     * @return {@link ResponseEntity} containing the order details wrapped in {@link ApiResponse}
     */
    @GetMapping("/{order-id}")
    ResponseEntity<ApiResponse<OrderResponse>> findById(
            @PathVariable("order-id") final Integer orderId
    );
}

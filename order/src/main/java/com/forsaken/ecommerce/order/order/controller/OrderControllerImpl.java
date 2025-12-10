package com.forsaken.ecommerce.order.order.controller;

import com.forsaken.ecommerce.common.responses.ApiResponse;
import com.forsaken.ecommerce.order.order.dto.OrderRequest;
import com.forsaken.ecommerce.order.order.dto.OrderResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class OrderControllerImpl implements IOrderController {

    @Override
    public ResponseEntity<ApiResponse<Integer>> createOrder(final OrderRequest request) throws ExecutionException, InterruptedException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<Integer>builder()
                                .status(ApiResponse.Status.SUCCESS)
                                .data(null)
                                .message("Order Created Successfully.")
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<List<OrderResponse>>> findAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.<List<OrderResponse>>builder()
                                .status(ApiResponse.Status.SUCCESS)
                                .data(null)
                                .message("Find All Orders.")
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<List<OrderResponse>>> findAllOrdersByCustomerId(
            final String customerId,
            final LocalDateTime fromDate,
            final LocalDateTime toDate
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.<List<OrderResponse>>builder()
                                .status(ApiResponse.Status.SUCCESS)
                                .data(null)
                                .message("Find All Orders By Customer Id.")
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<OrderResponse>> findById(final Integer orderId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.<OrderResponse>builder()
                                .status(ApiResponse.Status.SUCCESS)
                                .data(null)
                                .message("Find By Order Id.")
                                .build()
                );
    }
}

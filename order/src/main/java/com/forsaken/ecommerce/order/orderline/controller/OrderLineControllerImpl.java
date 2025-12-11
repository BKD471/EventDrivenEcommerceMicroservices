package com.forsaken.ecommerce.order.orderline.controller;

import com.forsaken.ecommerce.common.responses.ApiResponse;
import com.forsaken.ecommerce.order.orderline.dto.OrderLineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderLineControllerImpl implements IOrderLineController {
    @Override
    public ResponseEntity<ApiResponse<List<OrderLineResponse>>> findByOrderId(final Integer orderId) {
        return null;
    }
}

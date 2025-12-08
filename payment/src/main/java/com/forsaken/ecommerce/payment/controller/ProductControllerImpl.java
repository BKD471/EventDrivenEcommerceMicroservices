package com.forsaken.ecommerce.payment.controller;

import com.forsaken.ecommerce.common.responses.ApiResponse;
import com.forsaken.ecommerce.payment.dto.PaymentRequest;
import com.forsaken.ecommerce.payment.dto.PaymentSummaryDto;
import com.forsaken.ecommerce.payment.model.Payment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class ProductControllerImpl implements IProductController {

    @Override
    public ResponseEntity<ApiResponse<Integer>> createPayment(
            final PaymentRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<Integer>builder()
                                .status(ApiResponse.Status.SUCCESS)
                                .data(null) // TODO service calling to be done
                                .message("Payment Initiated")
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<List<PaymentSummaryDto>>> getPaymentSummary(
            final LocalDateTime fromDate,
            final LocalDateTime toDate
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<List<PaymentSummaryDto>>builder()
                                .status(ApiResponse.Status.SUCCESS)
                                .data(null) // TODO service calling to be done
                                .message("Payment Summary")
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<List<Payment>>> getAllPayments(
            final LocalDateTime fromDate,
            final LocalDateTime toDate
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<List<Payment>>builder()
                                .status(ApiResponse.Status.SUCCESS)
                                .data(null) // TODO service calling to be done
                                .message("Fetched Payments")
                                .build()
                );
    }
}

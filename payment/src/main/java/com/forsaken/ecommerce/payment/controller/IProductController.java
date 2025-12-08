package com.forsaken.ecommerce.payment.controller;


import com.forsaken.ecommerce.common.responses.ApiResponse;
import com.forsaken.ecommerce.payment.dto.PaymentRequest;
import com.forsaken.ecommerce.payment.dto.PaymentSummaryDto;
import com.forsaken.ecommerce.payment.model.Payment;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api/v1/payments")
public interface IProductController {

    /**
     * Creates a new payment record.
     *
     * <p>This endpoint accepts a {@link PaymentRequest} object containing all required
     * payment details. The request body is validated, and upon successful creation,
     * the API returns the generated payment ID.</p>
     *
     * @param request the payment creation request containing all necessary fields;
     *                must be valid and non-null
     * @return a ResponseEntity containing an ApiResponse wrapping the generated
     * payment ID (Integer)
     * @throws MethodArgumentNotValidException if the request body fails validation
     */
    @PostMapping
    ResponseEntity<ApiResponse<Integer>> createPayment(
            @RequestBody @Valid final PaymentRequest request
    );

    /**
     * Retrieves a summary of payments within the specified date range.
     *
     * <p>This endpoint returns aggregated payment information between the provided
     * <code>fromDate</code> and <code>toDate</code>. If no date parameters are supplied,
     * the full available dataset may be considered depending on service logic.</p>
     *
     * @param fromDate optional start date-time (ISO-8601 format). If null, no lower bound is applied.
     * @param toDate   optional end date-time (ISO-8601 format). If null, no upper bound is applied.
     * @return a ResponseEntity containing an ApiResponse wrapping a list of PaymentSummaryDto objects
     * representing summarized payment data.
     * @implNote Both dates must be in valid ISO date-time format: yyyy-MM-dd'T'HH:mm:ss.
     */
    @GetMapping("/summary")
    ResponseEntity<ApiResponse<List<PaymentSummaryDto>>> getPaymentSummary(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime toDate
    );

    /**
     * Retrieves all payment records within an optional date range.
     *
     * <p>If both <code>fromDate</code> and <code>toDate</code> are provided, payments
     * created within that date-time interval are returned. When no date parameters
     * are supplied, all available payment records are retrieved.</p>
     *
     * @param fromDate optional start date-time filter (ISO-8601 format). If null, no lower bound is applied.
     * @param toDate   optional end date-time filter (ISO-8601 format). If null, no upper bound is applied.
     * @return a ResponseEntity containing an ApiResponse wrapping a list of Payment objects
     */
    @GetMapping("/allPayments")
    ResponseEntity<ApiResponse<List<Payment>>> getAllPayments(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime toDate
    );
}

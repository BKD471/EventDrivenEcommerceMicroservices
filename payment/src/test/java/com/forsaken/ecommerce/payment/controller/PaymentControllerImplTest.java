package com.forsaken.ecommerce.payment.controller;


import com.forsaken.ecommerce.avro.PaymentMethod;
import com.forsaken.ecommerce.common.responses.ApiResponse;
import com.forsaken.ecommerce.common.responses.PagedResponse;
import com.forsaken.ecommerce.payment.dto.Customer;
import com.forsaken.ecommerce.payment.dto.PaymentRequest;
import com.forsaken.ecommerce.payment.dto.PaymentSummaryDto;
import com.forsaken.ecommerce.payment.model.Payment;
import com.forsaken.ecommerce.payment.service.IPaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for PaymentControllerImpl.
 */
class PaymentControllerImplTest {

    private IPaymentService paymentService;
    private PaymentControllerImpl controller;

    @BeforeEach
    void setUp() {
        paymentService = mock(IPaymentService.class);
        controller = new PaymentControllerImpl(paymentService);
    }

    @Test
    void createPayment_ReturnsCreatedAndApiResponse() {
        // Given
        final PaymentRequest request = PaymentRequest.builder()
                .amount(new BigDecimal(100))
                .paymentMethod(PaymentMethod.BITCOIN)
                .orderId(123)
                .orderReference("test_order_123")
                .customer(
                        Customer.builder().build()
                )
                .build();
        final int expectedPaymentId = 123;
        when(paymentService.createPayment(request)).thenReturn(expectedPaymentId);

        // When
        final ResponseEntity<ApiResponse<Integer>> response = controller.createPayment(request);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        final ApiResponse<Integer> body = response.getBody();
        assertNotNull(body);
        assertEquals(ApiResponse.Status.SUCCESS, body.status());
        assertEquals(expectedPaymentId, body.data());
        assertEquals("Payment Initiated", body.message());
        verify(paymentService, times(1)).createPayment(request);
        verifyNoMoreInteractions(paymentService);
    }

    @Test
    void getPaymentSummary_ReturnsCreatedAndApiResponse() {
        // Given
        final LocalDateTime fromDate = LocalDateTime.of(2025, 1, 1, 0, 0);
        final LocalDateTime toDate = LocalDateTime.of(2025, 1, 31, 23, 59);
        final int page = 0;
        final int size = 10;
        @SuppressWarnings("unchecked")
        final PagedResponse<PaymentSummaryDto> pagedResponse = mock(PagedResponse.class);
        when(paymentService.getPaymentSummary(fromDate, toDate, page, size))
                .thenReturn(pagedResponse);

        // When
        final ResponseEntity<ApiResponse<PagedResponse<PaymentSummaryDto>>> response =
                controller.getPaymentSummary(fromDate, toDate, page, size);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        final ApiResponse<PagedResponse<PaymentSummaryDto>> body = response.getBody();
        assertNotNull(body);
        assertEquals(ApiResponse.Status.SUCCESS, body.status());
        assertSame(pagedResponse, body.data());
        assertEquals("Payment Summary", body.message());
        verify(paymentService, times(1))
                .getPaymentSummary(fromDate, toDate, page, size);
        verifyNoMoreInteractions(paymentService);
    }

    @Test
    void getAllPayments_ReturnsCreatedAndApiResponse() {
        // Given
        final LocalDateTime fromDate = LocalDateTime.of(2025, 2, 1, 0, 0);
        final LocalDateTime toDate = LocalDateTime.of(2025, 2, 28, 23, 59);
        final int page = 1;
        final int size = 20;
        @SuppressWarnings("unchecked")
        final PagedResponse<Payment> pagedPayments = mock(PagedResponse.class);
        when(paymentService.getAllPayments(fromDate, toDate, page, size))
                .thenReturn(pagedPayments);

        // When
        final ResponseEntity<ApiResponse<PagedResponse<Payment>>> response =
                controller.getAllPayments(fromDate, toDate, page, size);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        final ApiResponse<PagedResponse<Payment>> body = response.getBody();
        assertNotNull(body);
        assertEquals(ApiResponse.Status.SUCCESS, body.status());
        assertSame(pagedPayments, body.data());
        assertEquals("Fetched Payments", body.message());
        verify(paymentService, times(1))
                .getAllPayments(fromDate, toDate, page, size);
        verifyNoMoreInteractions(paymentService);
    }
}

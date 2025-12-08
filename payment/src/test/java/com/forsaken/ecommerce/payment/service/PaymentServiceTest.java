package com.forsaken.ecommerce.payment.service;


import com.forsaken.ecommerce.avro.PaymentConfirmation;
import com.forsaken.ecommerce.avro.PaymentMethod;
import com.forsaken.ecommerce.common.responses.PagedResponse;
import com.forsaken.ecommerce.payment.dto.Customer;
import com.forsaken.ecommerce.payment.dto.PaymentRequest;
import com.forsaken.ecommerce.payment.dto.PaymentSummaryDto;
import com.forsaken.ecommerce.payment.model.Payment;
import com.forsaken.ecommerce.payment.repository.IPaymentRepository;
import com.forsaken.ecommerce.payment.repository.PaymentSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private IPaymentRepository repository;

    @Mock
    private INotificationProducerService notificationProducer;

    private PaymentServiceImpl service;

    @BeforeEach
    void setup() {
        service = new PaymentServiceImpl(repository, notificationProducer);
    }


    @Test
    void testCreatePayment() {
        // Given
        final PaymentRequest paymentRequest = constructPaymentRequest(1);
        final Payment payment = paymentRequest.toPayment();
        final ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        doReturn(payment).when(repository).save(paymentCaptor.capture());

        // When
        final Integer result = service.createPayment(paymentRequest);

        // Then
        assertEquals(1, result);
        final Payment passedToSave = paymentCaptor.getValue();
        assertEquals(new BigDecimal("150.50"), passedToSave.getAmount());
        assertEquals(PaymentMethod.PAYPAL, passedToSave.getPaymentMethod());

        // Capture notification
        final ArgumentCaptor<PaymentConfirmation> notifCaptor =
                ArgumentCaptor.forClass(PaymentConfirmation.class);
        verify(notificationProducer).sendNotification(notifCaptor.capture());
        final PaymentConfirmation paymentConfirmation = notifCaptor.getValue();
        assertEquals("order-123", paymentConfirmation.getOrderReference());
        assertEquals("John", paymentConfirmation.getCustomerFirstname());
    }


    @Test
    void testGetPaymentSummary() {
        // Given
        final LocalDateTime fromDate = LocalDateTime.now().minusDays(5);
        final LocalDateTime toDate = LocalDateTime.now();
        final PageRequest pageable = PageRequest.of(0, 10);

        final PaymentSummary paypal = new PaymentSummary() {
            public PaymentMethod getPaymentMethod() {
                return PaymentMethod.PAYPAL;
            }

            public Long getCount() {
                return 2L;
            }

            public BigDecimal getTotalAmount() {
                return new BigDecimal("300");
            }
        };
        final Page<PaymentSummary> page = new PageImpl<>(List.of(paypal));
        when(repository.findPaymentSummaryBetween(eq(fromDate), eq(toDate), eq(pageable)))
                .thenReturn(page);

        // When
        final PagedResponse<PaymentSummaryDto> result =
                service.getPaymentSummary(fromDate, toDate, 1, 10);

        // Then
        assertEquals(1, result.content().size());
        assertEquals(PaymentMethod.PAYPAL, result.content().get(0).paymentMethod());
        assertEquals(2L, result.content().get(0).count());
        assertEquals(new BigDecimal("300"), result.content().get(0).totalAmount());
    }


    @Test
    void testGetAllPayments() {
        // Given
        final LocalDateTime fromDate = LocalDateTime.now().minusDays(5);
        final LocalDateTime toDate = LocalDateTime.now();
        final PageRequest pageable = PageRequest.of(0, 5);
        final Payment paymentOne = constructPaymentRequest(1).toPayment();
        final Payment paymentTwo = constructPaymentRequest(2).toPayment();
        final Page<Payment> page = new PageImpl<>(List.of(paymentOne, paymentTwo), pageable, 2);
        when(repository.findAllByCreatedDateBetween(eq(fromDate), eq(toDate), eq(pageable)))
                .thenReturn(page);

        // When
        PagedResponse<Payment> result =
                service.getAllPayments(fromDate, toDate, 1, 5);

        // Then
        assertEquals(2, result.content().size());
        assertEquals(1, result.page()); // +1 from Page index
        assertEquals(5, result.size());
        assertEquals(2, result.totalElements());
    }

    private PaymentRequest constructPaymentRequest(final int paymentId) {
        return PaymentRequest.builder()
                .id(paymentId)
                .amount(new BigDecimal("150.50"))
                .paymentMethod(PaymentMethod.PAYPAL)
                .orderId(123)
                .orderReference("order-123")
                .customer(constructCustomer())
                .build();
    }

    private Customer constructCustomer() {
        return Customer.builder()
                .id("cust_123")
                .firstname("John")
                .lastname("Doe")
                .email("john@doe.com")
                .build();
    }
}

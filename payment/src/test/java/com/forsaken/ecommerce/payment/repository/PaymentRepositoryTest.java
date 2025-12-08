package com.forsaken.ecommerce.payment.repository;


import com.forsaken.ecommerce.avro.PaymentMethod;
import com.forsaken.ecommerce.payment.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentRepositoryTest {

    @Mock
    private IPaymentRepository paymentRepository;

    private LocalDateTime dateOne;
    private LocalDateTime dateTwo;
    private LocalDateTime dateThree;

    @BeforeEach
    void setup() {
        dateOne = LocalDateTime.of(2025, 1, 1, 10, 0);
        dateTwo = LocalDateTime.of(2025, 1, 5, 12, 0);
        dateThree = LocalDateTime.of(2025, 1, 10, 20, 0);
    }

    @Test
    void testFindPaymentSummaryBetween() {
        // Given
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

        final PaymentSummary card = new PaymentSummary() {
            public PaymentMethod getPaymentMethod() {
                return PaymentMethod.CREDIT_CARD;
            }

            public Long getCount() {
                return 1L;
            }

            public BigDecimal getTotalAmount() {
                return new BigDecimal("500");
            }
        };
        final Page<PaymentSummary> page = new PageImpl<>(List.of(paypal, card));
        final PageRequest request = PageRequest.of(0, 3);
        when(paymentRepository.findPaymentSummaryBetween(
                eq(dateOne),
                eq(dateThree),
                eq(request)
        )).thenReturn(page);

        // When
        final Page<PaymentSummary> result =
                paymentRepository.findPaymentSummaryBetween(dateOne, dateThree, request);

        // Then
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testFindAllByCreatedDateBetween() {
        // Given
        final Payment paymentOne = mock(Payment.class);
        final Payment paymentTwo = mock(Payment.class);
        final Page<Payment> page = new PageImpl<>(List.of(paymentOne, paymentTwo));
        final PageRequest request = PageRequest.of(0, 10);
        when(paymentRepository.findAllByCreatedDateBetween(
                eq(dateOne),
                eq(dateTwo),
                eq(request)
        )).thenReturn(page);

        // When
        final Page<Payment> result =
                paymentRepository.findAllByCreatedDateBetween(dateOne, dateTwo, request);

        // Then
        assertEquals(2, result.getTotalElements());
    }
}

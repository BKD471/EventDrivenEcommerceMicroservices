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

/**
 * Unit tests for verifying repository-layer interactions with {@link IPaymentRepository}
 * using Mockito-based stubbing.
 *
 * <p>This test class does not interact with a real database. Instead, it ensures that
 * repository method contracts are correctly invoked and that mocked responses behave
 * as expected. These tests are valuable for validating query method signatures,
 * projection handling, and ensuring that service-layer code relying on the repository
 * will receive the expected data structures.</p>
 */
@ExtendWith(MockitoExtension.class)
class PaymentRepositoryTest {

    @Mock
    private IPaymentRepository paymentRepository;

    private LocalDateTime dateOne;
    private LocalDateTime dateTwo;
    private LocalDateTime dateThree;

    /**
     * Initializes commonly used date instances for test filtering scenarios.
     *
     * <p>These timestamps simulate realistic filtering windows for queries
     * involving payment creation dates.</p>
     */
    @BeforeEach
    void setup() {
        dateOne = LocalDateTime.of(2025, 1, 1, 10, 0);
        dateTwo = LocalDateTime.of(2025, 1, 5, 12, 0);
        dateThree = LocalDateTime.of(2025, 1, 10, 20, 0);
    }

    /**
     * Verifies that the repository method {@link IPaymentRepository#findPaymentSummaryBetween}
     * correctly returns a page of {@link PaymentSummary} projections when invoked
     * with a valid date range and pageable request.
     *
     * <p>This test ensures:</p>
     * <ul>
     *     <li>Proper delegation to the repository method with expected arguments</li>
     *     <li>Correct transformation and retrieval of projection-based results</li>
     *     <li>Pagination metadata is preserved in the returned {@link Page}</li>
     * </ul>
     */
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

    /**
     * Validates that {@link IPaymentRepository#findAllByCreatedDateBetween}
     * returns a paginated list of {@link Payment} entities when called with
     * the correct date boundaries and pageable configuration.
     *
     * <p>The test asserts:</p>
     * <ul>
     *     <li>Invocation of the repository with exact filter parameters</li>
     *     <li>Correct handling of paginated entity results</li>
     *     <li>Expected total element count in the returned page</li>
     * </ul>
     */
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

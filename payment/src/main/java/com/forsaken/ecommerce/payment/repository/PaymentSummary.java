package com.forsaken.ecommerce.payment.repository;

import com.forsaken.ecommerce.avro.PaymentMethod;
import com.forsaken.ecommerce.payment.model.Payment;

import java.math.BigDecimal;

/**
 * Projection interface representing an aggregated summary of payments
 * grouped by payment method.
 *
 * <p>This interface is used by Spring Data JPA to map results from custom
 * summary queries (e.g., grouping by payment method). Each instance of this
 * projection corresponds to one payment method and contains:</p>
 *
 * <ul>
 *     <li><b>paymentMethod</b> – the payment method being summarized</li>
 *     <li><b>count</b> – total number of payments made using this method</li>
 *     <li><b>totalAmount</b> – sum of all payment amounts for this method</li>
 * </ul>
 *
 * <p>Spring automatically binds column aliases from the query to these method names.</p>
 *
 * @see Payment
 * @see PaymentMethod
 */
public interface PaymentSummary {

    /**
     * Returns the payment method for which this summary is generated.
     *
     * @return the payment method enum value
     */
    PaymentMethod getPaymentMethod();

    /**
     * Returns the number of payments recorded for the corresponding payment method.
     *
     * @return total count of payments
     */
    Long getCount();

    /**
     * Returns the aggregated total amount for all payments associated with
     * the payment method represented in this summary.
     *
     * @return total payment amount as BigDecimal
     */
    BigDecimal getTotalAmount();
}
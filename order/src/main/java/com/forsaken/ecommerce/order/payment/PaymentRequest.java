package com.forsaken.ecommerce.order.payment;


import com.forsaken.ecommerce.order.customer.CustomerResponse;
import com.forsaken.ecommerce.order.order.model.PaymentMethod;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}

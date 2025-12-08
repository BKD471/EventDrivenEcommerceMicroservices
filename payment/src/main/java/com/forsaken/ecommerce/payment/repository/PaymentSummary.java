package com.forsaken.ecommerce.payment.repository;

import com.forsaken.ecommerce.avro.PaymentMethod;

import java.math.BigDecimal;

public interface PaymentSummary {
    PaymentMethod getPaymentMethod();

    Long getCount();

    BigDecimal getTotalAmount();
}
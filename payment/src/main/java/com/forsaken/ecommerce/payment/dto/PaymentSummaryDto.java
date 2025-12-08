package com.forsaken.ecommerce.payment.dto;

import com.forsaken.ecommerce.avro.PaymentMethod;

import java.io.Serializable;
import java.math.BigDecimal;

public record PaymentSummaryDto(
        PaymentMethod paymentMethod,
        Long count,
        BigDecimal totalAmount
) implements Serializable {
}

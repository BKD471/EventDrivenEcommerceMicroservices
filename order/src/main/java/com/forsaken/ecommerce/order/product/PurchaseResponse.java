package com.forsaken.ecommerce.order.product;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PurchaseResponse(
        Integer productId,
        String name,
        String description,
        BigDecimal price,
        double quantity
) {
}
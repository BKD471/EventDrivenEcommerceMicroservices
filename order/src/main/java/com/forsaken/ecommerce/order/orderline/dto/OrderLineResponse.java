package com.forsaken.ecommerce.order.orderline.dto;

import lombok.Builder;

@Builder
public record OrderLineResponse(
        Integer id,
        double quantity
) {
}

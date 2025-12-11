package com.forsaken.ecommerce.order.orderline.dto;

import com.forsaken.ecommerce.order.order.model.Order;
import com.forsaken.ecommerce.order.orderline.model.OrderLine;

public record OrderLineRequest(
        Integer id,
        Integer orderId,
        Integer productId,
        double quantity
) {

    public OrderLine toOrderLine() {
        return OrderLine.builder()
                .id(this.id())
                .productId(this.productId())
                .order(
                        Order.builder()
                                .id(this.orderId())
                                .build()
                )
                .quantity(this.quantity())
                .build();
    }
}

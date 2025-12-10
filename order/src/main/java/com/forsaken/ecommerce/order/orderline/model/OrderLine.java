package com.forsaken.ecommerce.order.orderline.model;

import com.forsaken.ecommerce.order.order.model.Order;
import com.forsaken.ecommerce.order.orderline.dto.OrderLineResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Entity
@NoArgsConstructor
@Table(name = "customer_line")
public class OrderLine {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Integer productId;
    private double quantity;

    public OrderLineResponse toOrderLineResponse() {
        return OrderLineResponse.builder()
                .id(this.getId())
                .quantity(this.getQuantity())
                .build();
    }
}

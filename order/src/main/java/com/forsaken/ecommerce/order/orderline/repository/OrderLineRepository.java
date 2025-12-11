package com.forsaken.ecommerce.order.orderline.repository;

import com.forsaken.ecommerce.order.orderline.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {
    List<OrderLine> findAllByOrderId(final Integer orderId);
}

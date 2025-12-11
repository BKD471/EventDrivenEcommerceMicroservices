package com.forsaken.ecommerce.order.orderline.service;

import com.forsaken.ecommerce.order.orderline.dto.OrderLineRequest;
import com.forsaken.ecommerce.order.orderline.dto.OrderLineResponse;

import java.util.List;

/**
 * Service interface responsible for handling business operations
 * related to Order Line entities. Provides functionality to create
 * new order lines and retrieve order lines associated with a specific order.
 */
public interface IOrderLineService {

    /**
     * Persists a new order line based on the provided request details.
     *
     * <p>This method performs all business validations required before
     * saving the order line. If validation fails, an appropriate exception
     * should be thrown by the implementation.</p>
     *
     * @param request the order line creation payload containing product,
     *                quantity, price, and other relevant details
     * @throws jakarta.validation.ConstraintViolationException if the request violates validation constraints
     */
    void saveOrderLine(final OrderLineRequest request);

    /**
     * Retrieves all order lines for a given order.
     *
     * <p>If the specified orderId does not exist, the implementation may return
     * an empty list or throw a custom exception depending on the business rule.</p>
     *
     * @param orderId the unique identifier of the order whose order lines
     *                are to be retrieved; must not be {@code null}
     * @return list of {@link OrderLineResponse} belonging to the specified order
     * @throws IllegalArgumentException if orderId is {@code null}
     */
    List<OrderLineResponse> findAllByOrderId(final Integer orderId);
}

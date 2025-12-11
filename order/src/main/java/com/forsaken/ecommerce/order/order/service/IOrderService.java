package com.forsaken.ecommerce.order.order.service;

import com.forsaken.ecommerce.common.exceptions.CustomerNotFoundExceptions;
import com.forsaken.ecommerce.order.order.dto.OrderRequest;
import com.forsaken.ecommerce.order.order.dto.OrderResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Service interface for managing Orders within the system.
 * <p>
 * Provides operations for creating new orders, retrieving orders by
 * various filters, and fetching detailed information for a specific order.
 * This service acts as the core business layer for order operations.
 */
public interface IOrderService {

    /**
     * Creates a new order based on the provided request payload.
     *
     * @param request the order creation data containing customer details,
     *                order lines, payment info, etc.
     * @return the unique identifier of the newly created order.
     * @throws ExecutionException   if an asynchronous processing error occurs
     *                              (e.g., event publishing).
     * @throws InterruptedException if the thread executing the operation is interrupted.
     */
    Integer createOrder(final OrderRequest request) throws ExecutionException, InterruptedException, CustomerNotFoundExceptions;

    /**
     * Retrieves all orders available in the system.
     *
     * @return a list of {@link OrderResponse} containing summary or detailed
     * information for each order.
     */
    List<OrderResponse> findAllOrders();

    /**
     * Finds a single order using its unique identifier.
     *
     * @param id the order ID to search for.
     * @return the {@link OrderResponse} representation of the order.
     * @throws jakarta.persistence.EntityNotFoundException if the order does not exist.
     */
    OrderResponse findById(final Integer id);

    /**
     * Retrieves all orders associated with a specific customer.
     * Optional date filters may be applied to restrict results to a time range.
     *
     * @param customerId the unique identifier of the customer whose orders
     *                   should be fetched.
     * @param fromDate   (optional) start of the creation date range filter;
     *                   may be {@code null}.
     * @param toDate     (optional) end of the creation date range filter;
     *                   may be {@code null}.
     * @return a list of {@link OrderResponse} belonging to the specified customer,
     * optionally filtered by date.
     */
    List<OrderResponse> findAllOrdersByCustomerId(
            final String customerId,
            final LocalDateTime fromDate,
            final LocalDateTime toDate
    );
}

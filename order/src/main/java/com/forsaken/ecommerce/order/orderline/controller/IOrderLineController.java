package com.forsaken.ecommerce.order.orderline.controller;

import com.forsaken.ecommerce.common.responses.ApiResponse;
import com.forsaken.ecommerce.order.orderline.dto.OrderLineResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * REST controller interface for managing Order Line resources.
 *
 * <p>This controller exposes operations to retrieve order line items
 * associated with a specific Order. It is mapped under the base path
 * <b>/api/v1/order-lines</b>.</p>
 */
@RequestMapping("/api/v1/order-lines")
@Validated
public interface IOrderLineController {

    /**
     * Retrieves all Order Line items for the given Order ID.
     *
     * <p>This endpoint returns a list of {@link OrderLineResponse}
     * objects representing individual items belonging to the specified order.
     * The caller must provide a valid <code>orderId</code> path variable.
     * If the order does not exist or contains no items, an empty list is returned
     * within the API response wrapper.</p>
     *
     * @param orderId the unique identifier of the Order whose line items should be fetched;
     *                must not be {@code null}
     *
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} wrapping
     *         the list of {@link OrderLineResponse}
     *
     * <p><b>Possible Responses:</b></p>
     * <ul>
     *     <li><b>200 OK</b> – Successfully retrieved order line items.</li>
     *     <li><b>400 Bad Request</b> – Invalid order ID supplied.</li>
     *     <li><b>404 Not Found</b> – Order not found in the system.</li>
     *     <li><b>500 Internal Server Error</b> – Unexpected server failure.</li>
     * </ul>
     */
    @GetMapping("/order/{order-id}")
    ResponseEntity<ApiResponse<List<OrderLineResponse>>> findByOrderId(
            @NotNull @PathVariable("order-id") final Integer orderId
    );
}

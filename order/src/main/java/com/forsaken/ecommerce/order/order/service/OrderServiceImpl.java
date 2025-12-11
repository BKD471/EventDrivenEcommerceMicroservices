package com.forsaken.ecommerce.order.order.service;

import com.forsaken.ecommerce.common.exceptions.CustomerNotFoundExceptions;
import com.forsaken.ecommerce.order.customer.CustomerService;
import com.forsaken.ecommerce.order.customer.ICustomerService;
import com.forsaken.ecommerce.order.order.dto.OrderRequest;
import com.forsaken.ecommerce.order.order.dto.OrderResponse;
import com.forsaken.ecommerce.order.order.model.Order;
import com.forsaken.ecommerce.order.order.repository.IOrderRepository;
import com.forsaken.ecommerce.order.orderline.service.IOrderLineService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IOrderLineService orderLineService;
    private final ICustomerService customerService;
    private final Class<?> className = OrderServiceImpl.class;

    @Override
    public Integer createOrder(final OrderRequest request) throws ExecutionException, InterruptedException, CustomerNotFoundExceptions {
        log.info("Creating Order Request: {}", request);
        final var fetchedCustomer = customerService.getCustomer(request.customerId());
        final var fetchedPurchasedProducts = CompletableFuture.completedFuture(Optional.empty()); // TODO call Product Service
        CompletableFuture.allOf(fetchedCustomer, fetchedPurchasedProducts).join();

        final var customer = fetchedCustomer.get()
                .orElseThrow(() -> new CustomerNotFoundExceptions(
                                "Cannot create order:: No customer exists with the provided ID",
                                "createOrder(final OrderRequest request) in " + className
                        )
                );
        final var purchasedProducts = fetchedPurchasedProducts.get();
        final var order = this.orderRepository.save(request.toOrder());

        // TODO save order line

        // TODO call payment service

        // TODO create order confirmation avro object for publishinging to kafka

        // TODO send/publish Order Confirmation avro object

        return order.getId();
    }

    @Override
    public List<OrderResponse> findAllOrders() {
        log.info("Finding All Orders");
        return this.orderRepository.findAll()
                .stream()
                .map(Order::fromOrder)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse findById(final Integer id) {
        log.info("Finding Order by ID: {}", id);
        return this.orderRepository.findById(id)
                .map(Order::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException
                        (String.format("No order found with the provided ID: %d", id))
                );
    }

    @Override
    public List<OrderResponse> findAllOrdersByCustomerId(
            final String customerId,
            final LocalDateTime fromDate,
            final LocalDateTime toDate
    ) {
        log.info("Finding All Orders By Customer: {}", customerId);
        return orderRepository.findAllByCustomerIdAndCreatedDateBetween(customerId, fromDate, toDate)
                .stream().map(Order::fromOrder).toList();
    }
}

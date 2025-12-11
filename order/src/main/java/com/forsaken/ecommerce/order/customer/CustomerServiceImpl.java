package com.forsaken.ecommerce.order.customer;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements ICustomerService {

    private final ICustomerClient customerClient;

    @Override
    public CompletableFuture<Optional<CustomerResponse>> getCustomer(final String customerId) {
        log.info("Get Customer by ID: {}", customerId);
        return CompletableFuture.completedFuture(customerClient.findCustomerById(customerId));
    }
}
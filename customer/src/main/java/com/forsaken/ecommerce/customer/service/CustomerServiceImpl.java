package com.forsaken.ecommerce.customer.service;

import com.forsaken.ecommerce.common.exceptions.CustomerNotFoundExceptions;
import com.forsaken.ecommerce.customer.dto.CustomerRequest;
import com.forsaken.ecommerce.customer.dto.CustomerResponse;
import com.forsaken.ecommerce.customer.model.Customer;
import com.forsaken.ecommerce.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final Class<?> className = CustomerServiceImpl.class;

    @Override
    public String createCustomer(CustomerRequest request) {
        log.info("Creating customer with request {}", request);
        final String customerId = UUID.randomUUID().toString();
        customerRepository.save(request.toCustomer(customerId));
        return customerId;
    }

    @Override
    public String updateCustomer(CustomerRequest request) throws CustomerNotFoundExceptions {
        log.info("Received request to update customer {}", request);
        Customer customer = this.customerRepository.findById(request.id())
                .orElseThrow(() -> new CustomerNotFoundExceptions(
                        String.format("Cannot update customer:: No customer found with the provided ID: %s", request.id()),
                        "updateCustomer(CustomerRequest request) in " + className
                ));
        mergeCustomer(customer, request);
        this.customerRepository.save(customer);
        log.info("Updated customer with id {}", customer.getId());
        return String.format("Updated customer with id %s", customer.getId());
    }

    @Override
    public List<CustomerResponse> findAllCustomers() {
        log.info("Received request to get all customers");
        return this.customerRepository.findAll()
                .stream()
                .map(Customer::fromCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse findById(final String customerId) throws CustomerNotFoundExceptions {
        log.info("Received request to get customer by ID {}", customerId);
        return this.customerRepository.findById(customerId)
                .map(Customer::fromCustomer)
                .orElseThrow(() -> new CustomerNotFoundExceptions(
                                String.format("No customer found with the provided ID: %s", customerId),
                                "findById(final String customerId) in " + className
                        )
                );
    }

    @Override
    public boolean existsById(final String customerId) {
        log.info("Received request to check if customer with id {}", customerId);
        return this.customerRepository.findById(customerId)
                .isPresent();
    }

    @Override
    public String deleteCustomer(final String customerId) {
        log.info("Received request to delete customer with id {}", customerId);
        this.customerRepository.deleteById(customerId);
        return String.format("Deleted customer with id %s", customerId);
    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
        if (StringUtils.isNotBlank(request.firstname())) {
            customer.setFirstName(request.firstname());
        }
        if (StringUtils.isNotBlank(request.lastname())) {
            customer.setLastName(request.lastname());
        }
        if (StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if (request.address() != null) {
            customer.setAddress(request.address());
        }
    }
}

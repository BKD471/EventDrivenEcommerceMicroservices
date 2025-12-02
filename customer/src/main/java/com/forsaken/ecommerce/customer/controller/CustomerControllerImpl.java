package com.forsaken.ecommerce.customer.controller;


import com.forsaken.ecommerce.common.exceptions.CustomerNotFoundExceptions;
import com.forsaken.ecommerce.common.responses.ApiResponse;
import com.forsaken.ecommerce.customer.dto.CustomerRequest;
import com.forsaken.ecommerce.customer.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class CustomerControllerImpl implements ICustomerController {

    private final ICustomerService customerService;

    @Override
    public ResponseEntity<ApiResponse<?>> createCustomer(final CustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.builder()
                                .status(ApiResponse.Status.SUCCESS)
                                .data(customerService.createCustomer(request))
                                .message("Customer Created")
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<?>> updateCustomer(final CustomerRequest request) throws CustomerNotFoundExceptions {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(
                        ApiResponse.builder()
                                .status(ApiResponse.Status.SUCCESS)
                                .data(customerService.updateCustomer(request))
                                .message("Customer Updated")
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<?>> findAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.builder()
                                .status(ApiResponse.Status.SUCCESS)
                                .data(customerService.findAllCustomers())
                                .message("All Customers Data Fetched")
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<?>> existsById(final String customerId) {
        final boolean exists = customerService.existsById(customerId);
        final String message = exists ? "Customer " + customerId + " exists" : "Customer " + customerId + " not found";
        final ApiResponse.Status status = exists ? ApiResponse.Status.SUCCESS : ApiResponse.Status.FAILED;

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.builder()
                                .status(status)
                                .data(exists)
                                .message(message)
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<?>> findById(final String customerId) throws CustomerNotFoundExceptions {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.builder()
                                .status(ApiResponse.Status.SUCCESS)
                                .data(customerService.findById(customerId))
                                .message("Customer data Fetched for the id " + customerId)
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<?>> delete(final String customerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.builder()
                                .status(ApiResponse.Status.SUCCESS)
                                .data(customerService.deleteCustomer(customerId))
                                .message("Customer Deleted for the id " + customerId)
                                .build()
                );
    }
}

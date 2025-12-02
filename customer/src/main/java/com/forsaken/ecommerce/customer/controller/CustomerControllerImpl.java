package com.forsaken.ecommerce.customer.controller;


import com.forsaken.ecommerce.common.responses.ApiResponse;
import com.forsaken.ecommerce.customer.dto.CustomerRequest;
import com.forsaken.ecommerce.customer.dto.CustomerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerControllerImpl implements ICustomerController {
    @Override
    public ResponseEntity<ApiResponse<String>> createCustomer(final CustomerRequest request) {
        // TODO call the service
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<String>> updateCustomer(final CustomerRequest request) {
        // TODO call the service
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> findAll() {
        // TODO call the service
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<Boolean>> existsById(final String customerId) {
        // TODO call the service
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<CustomerResponse>> findById(final String customerId) {
        // TODO call the service
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<String>> delete(final String customerId) {
        // TODO call the service
        return null;
    }
}

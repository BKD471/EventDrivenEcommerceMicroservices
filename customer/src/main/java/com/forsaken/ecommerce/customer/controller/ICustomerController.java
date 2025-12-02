package com.forsaken.ecommerce.customer.controller;

import com.forsaken.ecommerce.common.responses.ApiResponse;
import com.forsaken.ecommerce.customer.dto.CustomerRequest;
import com.forsaken.ecommerce.customer.dto.CustomerResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/v1/customers")
public interface ICustomerController {

    /**
     * this service creates customer in database.
     *
     * @param request - request to create customer
     * @return ApiResponse<String> - acknowledgment that customer has been created
     */
    @PostMapping("/create")
    ResponseEntity<ApiResponse<String>> createCustomer(
            @RequestBody @Valid final CustomerRequest request
    );

    /**
     * this service update customer record in database.
     *
     * @param request - request to update customer
     * @return ApiResponse<String> - acknowledgment that customer data has been updated
     */
    @PutMapping
    ResponseEntity<ApiResponse<String>> updateCustomer(
            @RequestBody @Valid final CustomerRequest request
    );

    /**
     * this service fetches list of all customers present in database.
     *
     * @return ApiResponse<List<CustomerResponse>> - list of all customers present in database
     */
    @GetMapping
    ResponseEntity<ApiResponse<List<CustomerResponse>>> findAll();

    /**
     * this service checks whether customer exist in database or not.
     *
     * @param customerId - id of customer
     * @return ApiResponse<Boolean> - acknowledgment that customer exists in database
     */
    @GetMapping("/exists/{customer-id}")
    ResponseEntity<ApiResponse<Boolean>> existsById(
            @PathVariable("customer-id") @NotBlank final String customerId
    );

    /**
     * this service fetches customer data for the customerId from database.
     *
     * @param customerId - id of customer
     * @return ApiResponse<CustomerResponse> - customer data for the id - customerId
     */
    @GetMapping("/{customer-id}")
    ResponseEntity<ApiResponse<CustomerResponse>> findById(
            @PathVariable("customer-id") @NotBlank final String customerId
    );

    /**
     * this service deletes customer for the customerId from database.
     *
     * @param customerId - id of customer
     * @return ApiResponse<String> - acknowledgment that customer has been deleted from database
     */
    @DeleteMapping("/{customer-id}")
    ResponseEntity<ApiResponse<String>> delete(
            @PathVariable("customer-id") @NotBlank final String customerId
    );
}

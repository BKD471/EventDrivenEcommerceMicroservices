package com.forsaken.ecommerce.customer.controller;

import com.forsaken.ecommerce.common.exceptions.CustomerNotFoundExceptions;
import com.forsaken.ecommerce.common.responses.ApiResponse;
import com.forsaken.ecommerce.customer.dto.CustomerRequest;
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


@RequestMapping("/api/v1/customers")
public interface ICustomerController {

    /**
     * this service creates customer in database.
     *
     * @param request - request to create customer
     * @return ApiResponse<String> - acknowledgment that customer has been created with status code.
     */
    @PostMapping("/create")
    ResponseEntity<ApiResponse<?>> createCustomer(
            @RequestBody @Valid final CustomerRequest request
    );

    /**
     * this service update customer record in database.
     *
     * @param request - request to update customer
     * @return ApiResponse<String> - acknowledgment that customer data has been updated with status code.
     */
    @PutMapping
    ResponseEntity<ApiResponse<?>> updateCustomer(
            @RequestBody @Valid final CustomerRequest request
    ) throws CustomerNotFoundExceptions;

    /**
     * this service fetches list of all customers present in database.
     *
     * @return ApiResponse<List<CustomerResponse>> - list of all customers present in database with status code.
     */
    @GetMapping
    ResponseEntity<ApiResponse<?>> findAll();

    /**
     * this service checks whether customer exist in database or not.
     *
     * @param customerId - id of customer
     * @return ApiResponse<Boolean> - acknowledgment that customer exists in database with status code.
     */
    @GetMapping("/exists/{customer-id}")
    ResponseEntity<ApiResponse<?>> existsById(
            @PathVariable("customer-id") @NotBlank final String customerId
    );

    /**
     * this service fetches customer data for the customerId from database.
     *
     * @param customerId - id of customer
     * @return ApiResponse<CustomerResponse> - customer data for the id - customerId with status code.
     */
    @GetMapping("/{customer-id}")
    ResponseEntity<ApiResponse<?>> findById(
            @PathVariable("customer-id") @NotBlank final String customerId
    ) throws CustomerNotFoundExceptions;

    /**
     * this service deletes customer for the customerId from database.
     *
     * @param customerId - id of customer
     * @return ApiResponse<String> - acknowledgment that customer has been deleted from database with status code.
     */
    @DeleteMapping("/{customer-id}")
    ResponseEntity<ApiResponse<?>> delete(
            @PathVariable("customer-id") @NotBlank final String customerId
    );
}

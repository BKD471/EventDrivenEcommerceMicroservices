package com.forsaken.ecommerce.customer.service;

import com.forsaken.ecommerce.common.exceptions.CustomerNotFoundExceptions;
import com.forsaken.ecommerce.customer.dto.CustomerRequest;
import com.forsaken.ecommerce.customer.dto.CustomerResponse;

import java.util.List;

public interface ICustomerService {

    /**
     * this service creates customer in database.
     *
     * @param request - request to create customer
     * @return String - acknowledgment that customer has been created
     */
    String createCustomer(final CustomerRequest request);

    /**
     * this service update customer record in database.
     *
     * @param request - request to update customer
     * @return String - acknowledgment that customer data has been updated
     */
    String updateCustomer(final CustomerRequest request) throws CustomerNotFoundExceptions;

    /**
     * this service fetches list of all customers present in database.
     *
     * @return List<CustomerResponse> - list of all customers present in database
     */
    List<CustomerResponse> findAllCustomers();

    /**
     * this service fetches customer data for the customerId from database.
     *
     * @param customerId - id of customer
     * @return CustomerResponse - customer data for the id - customerId
     */
    CustomerResponse findById(final String customerId) throws CustomerNotFoundExceptions;


    /**
     * this service checks whether customer exist in database or not.
     *
     * @param customerId - id of customer
     * @return Boolean - acknowledgment that customer exists in database
     */
    boolean existsById(final String customerId);

    /**
     * this service deletes customer for the customerId from database.
     *
     * @param customerId - id of customer
     * @return ApiResponse<String> - acknowledgment that customer has been deleted from database
     */
    String deleteCustomer(final String customerId);
}

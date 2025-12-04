package com.forsaken.ecommerce.customer.controller;


import com.forsaken.ecommerce.common.exceptions.CustomerNotFoundExceptions;
import com.forsaken.ecommerce.common.responses.ApiResponse;
import com.forsaken.ecommerce.customer.dto.CustomerRequest;
import com.forsaken.ecommerce.customer.dto.CustomerResponse;
import com.forsaken.ecommerce.customer.model.Address;
import com.forsaken.ecommerce.customer.service.ICustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CustomerControllerImplTest {

    private static final String CUSTOMER_ID = "cust-123";
    private static final String CUSTOMER_EMAIL = "abc@gmail.com";

    @Mock
    private ICustomerService customerService;

    @InjectMocks
    private CustomerControllerImpl controller;


    @Test
    void createCustomer_ReturnsCreatedResponse() throws CustomerNotFoundExceptions {
        // Given
        final CustomerRequest request = constructCustomerRequest();
        final String serviceResponse = "created-customer-object";

        when(customerService.createCustomer(request)).thenReturn(serviceResponse);

        // When
        final ResponseEntity<ApiResponse<?>> resp = controller.createCustomer(request);

        // Then
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertNotNull(resp.getBody());
        final ApiResponse<?> body = resp.getBody();
        assertEquals(ApiResponse.Status.SUCCESS, body.status());
        assertEquals("Customer Created", body.message());
        assertEquals(serviceResponse, body.data());
        verify(customerService, times(1)).createCustomer(request);
    }

    @Test
    void updateCustomer_ReturnsAcceptedResponse() throws CustomerNotFoundExceptions {
        // Given
        final CustomerRequest request = constructCustomerRequest();
        final String serviceResponse = "updated-customer-object";

        when(customerService.updateCustomer(request)).thenReturn(serviceResponse);

        // When
        final ResponseEntity<ApiResponse<?>> resp = controller.updateCustomer(request);

        // Then
        assertEquals(HttpStatus.ACCEPTED, resp.getStatusCode());
        final ApiResponse<?> body = resp.getBody();
        assertNotNull(body);
        assertEquals(ApiResponse.Status.SUCCESS, body.status());
        assertEquals("Customer Updated", body.message());
        assertEquals(serviceResponse, body.data());
        verify(customerService, times(1)).updateCustomer(request);
    }

    @Test
    void findAll_ReturnsList() {
        // Given
        final List<CustomerResponse> serviceData = List.of(constructCustomerResponse(), constructCustomerResponse());
        when(customerService.findAllCustomers()).thenReturn(serviceData);

        // When
        final ResponseEntity<ApiResponse<?>> resp = controller.findAll();

        // Then
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        final ApiResponse<?> body = resp.getBody();
        assertNotNull(body);
        assertEquals(ApiResponse.Status.SUCCESS, body.status());
        assertEquals("All Customers Data Fetched", body.message());
        assertEquals(serviceData, body.data());
        verify(customerService, times(1)).findAllCustomers();
    }

    @Test
    void existsById_WhenExists_ReturnsSuccessStatus() {
        // Given
        when(customerService.existsById(CUSTOMER_ID)).thenReturn(true);

        // When
        final ResponseEntity<ApiResponse<?>> resp = controller.existsById(CUSTOMER_ID);

        // Then
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        final ApiResponse<?> body = resp.getBody();
        assertNotNull(body);
        assertEquals(ApiResponse.Status.SUCCESS, body.status());
        assertEquals(true, body.data());
        assertEquals("Customer " + CUSTOMER_ID + " exists", body.message());
        verify(customerService, times(1)).existsById(CUSTOMER_ID);
    }

    @Test
    void existsById_WhenNotExists_ReturnsFailedStatus() {
        // Given
        when(customerService.existsById(CUSTOMER_ID)).thenReturn(false);

        // When
        final ResponseEntity<ApiResponse<?>> resp = controller.existsById(CUSTOMER_ID);

        // Then
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        final ApiResponse<?> body = resp.getBody();
        assertNotNull(body);
        assertEquals(ApiResponse.Status.FAILED, body.status());
        assertEquals(false, body.data());
        assertEquals("Customer " + CUSTOMER_ID + " not found", body.message());
        verify(customerService, times(1)).existsById(CUSTOMER_ID);
    }

    @Test
    void findById_ReturnsCustomer_WhenFound() throws CustomerNotFoundExceptions {
        // Given
        final CustomerResponse serviceResult = constructCustomerResponse();
        when(customerService.findById(CUSTOMER_ID)).thenReturn(serviceResult);

        // When
        final ResponseEntity<ApiResponse<?>> resp = controller.findById(CUSTOMER_ID);

        // Then
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        final ApiResponse<?> body = resp.getBody();
        assertNotNull(body);
        assertEquals(ApiResponse.Status.SUCCESS, body.status());
        assertEquals("Customer data fetched for the id " + CUSTOMER_ID, body.message());
        assertEquals(serviceResult, body.data());
        verify(customerService, times(1)).findById(CUSTOMER_ID);
    }

    @Test
    void findById_PropagatesException_WhenNotFound() throws CustomerNotFoundExceptions {
        // Given
        when(customerService.findById(CUSTOMER_ID)).thenThrow(new CustomerNotFoundExceptions("not found", ""));

        // When / Then - controller method declares throws, so it should propagate
        assertThrows(CustomerNotFoundExceptions.class, () -> controller.findById(CUSTOMER_ID));
        verify(customerService, times(1)).findById(CUSTOMER_ID);
    }

    @Test
    void findByEmail_ReturnsCustomer_WhenFound() throws CustomerNotFoundExceptions {
        // Given
        final CustomerResponse serviceResult = constructCustomerResponse();
        when(customerService.findByEmail(CUSTOMER_EMAIL)).thenReturn(serviceResult);

        // When
        final ResponseEntity<ApiResponse<?>> resp = controller.findByEmail(CUSTOMER_EMAIL);

        // Then
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        final ApiResponse<?> body = resp.getBody();
        assertNotNull(body);
        assertEquals(ApiResponse.Status.SUCCESS, body.status());
        assertEquals("Customer data fetched for the emailId " + CUSTOMER_EMAIL, body.message());
        assertEquals(serviceResult, body.data());
        verify(customerService, times(1)).findByEmail(CUSTOMER_EMAIL);
    }

    @Test
    void delete_ReturnsSuccess() {
        // Given
        final String deleteResult = "deleted";
        when(customerService.deleteCustomer(CUSTOMER_ID)).thenReturn(deleteResult);

        // When
        final ResponseEntity<ApiResponse<?>> resp = controller.delete(CUSTOMER_ID);

        // Then
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        final ApiResponse<?> body = resp.getBody();
        assertNotNull(body);
        assertEquals(ApiResponse.Status.SUCCESS, body.status());
        assertEquals(deleteResult, body.data());
        assertEquals("Customer Deleted for the id " + CUSTOMER_ID, body.message());
        verify(customerService, times(1)).deleteCustomer(CUSTOMER_ID);
    }

    private CustomerRequest constructCustomerRequest() {
        return CustomerRequest.builder()
                .id(CUSTOMER_ID)
                .firstname("test-user-firstname-123")
                .lastname("test-user-lastname-123")
                .email(CUSTOMER_EMAIL)
                .address(constructAddress())
                .build();
    }

    private Address constructAddress() {
        return Address.builder()
                .street("Street-123")
                .houseNumber("houseNumber-123")
                .zipCode("zipCOde-123")
                .build();
    }


    private CustomerResponse constructCustomerResponse() {
        return CustomerResponse.builder()
                .id(CUSTOMER_ID)
                .firstname("test-user-firstname-123")
                .lastname("test-user-lastname-123")
                .email(CUSTOMER_EMAIL)
                .address(constructAddress())
                .build();
    }
}
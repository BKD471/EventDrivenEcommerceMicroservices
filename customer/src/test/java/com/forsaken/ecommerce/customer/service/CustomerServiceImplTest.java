package com.forsaken.ecommerce.customer.service;


import com.forsaken.ecommerce.common.exceptions.CustomerNotFoundExceptions;
import com.forsaken.ecommerce.customer.dto.CustomerRequest;
import com.forsaken.ecommerce.customer.dto.CustomerResponse;
import com.forsaken.ecommerce.customer.model.Address;
import com.forsaken.ecommerce.customer.model.Customer;
import com.forsaken.ecommerce.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    private static final String CUSTOMER_ID = "cust-123";
    private static final String EMAIL_EXISTING = "abc@gmail.com";
    private static final String EMAIL_NEW = "new@gmail.com";
    private static final String FIRST_NAME = "test-user-firstname-123";
    private static final String LAST_NAME = "test-user-lastname-123";

    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerRequest requestMock;


    @BeforeEach
    void setUp() {
        requestMock = mock(CustomerRequest.class);
    }


    @Test
    void createCustomer_ShouldReturnGeneratedId_WhenEmailNotPresent() throws CustomerNotFoundExceptions {
        // Given
        final CustomerRequest request = constructCustomerRequest();
        when(customerRepository.findByEmail(EMAIL_NEW)).thenReturn(Optional.empty());
        ArgumentCaptor<Customer> savedCaptor = ArgumentCaptor.forClass(Customer.class);

        // When
        final String generatedId = customerService.createCustomer(request);

        // Then
        assertNotNull(generatedId);
        verify(customerRepository, times(1)).save(savedCaptor.capture());
        Customer captured = savedCaptor.getValue();

        assertNotNull(captured);
        assertEquals(EMAIL_NEW, captured.getCustomerEmail());
    }


    @Test
    void createCustomer_ShouldThrow_WhenEmailAlreadyExists() {
        // Given
        final Customer existingCustomer = constructCustomer(CUSTOMER_ID, FIRST_NAME, LAST_NAME, EMAIL_EXISTING);
        when(requestMock.email()).thenReturn(EMAIL_EXISTING);
        when(customerRepository.findByEmail(EMAIL_EXISTING)).thenReturn(Optional.of(existingCustomer));

        // Then
        final CustomerNotFoundExceptions ex = assertThrows(CustomerNotFoundExceptions.class,
                () -> customerService.createCustomer(requestMock));
        assertTrue(ex.getMessage().contains("Customer is already present with the provided email"));
        verify(customerRepository, times(0)).save(any());
        verify(customerRepository, times(1)).findByEmail(EMAIL_EXISTING);
        verifyNoMoreInteractions(customerRepository);
    }


    @Test
    void updateCustomer_ShouldUpdate_WhenCustomerExists() throws CustomerNotFoundExceptions {
        // Given
        final Customer existingCustomer = constructCustomer(CUSTOMER_ID, FIRST_NAME, LAST_NAME, EMAIL_EXISTING);
        final Address customerAddress = constructAddress();
        when(requestMock.id()).thenReturn(CUSTOMER_ID);
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(existingCustomer));
        when(requestMock.firstname()).thenReturn(FIRST_NAME);
        when(requestMock.lastname()).thenReturn(LAST_NAME);
        when(requestMock.email()).thenReturn("updated@example.com");
        when(requestMock.address()).thenReturn(customerAddress);
        final ArgumentCaptor<Customer> savedCaptor = ArgumentCaptor.forClass(Customer.class);

        // When
        final String result = customerService.updateCustomer(requestMock);

        // Then
        assertTrue(result.contains(CUSTOMER_ID));
        verify(customerRepository, times(1)).save(savedCaptor.capture());
        Customer captured = savedCaptor.getValue();
        assertEquals(existingCustomer.getCustomerId(), captured.getCustomerId());
        assertEquals(FIRST_NAME, captured.getFirstName());
        assertEquals(LAST_NAME, captured.getLastName());
        assertEquals("updated@example.com", captured.getCustomerEmail());
        assertEquals(customerAddress, captured.getAddress());
        assertNotNull(captured);
    }


    @Test
    void updateCustomer_ShouldThrow_WhenCustomerNotFound() {
        // Given
        final Customer existingCustomer = constructCustomer(CUSTOMER_ID, FIRST_NAME, LAST_NAME, EMAIL_EXISTING);
        when(requestMock.id()).thenReturn(CUSTOMER_ID);
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

        // When
        assertThrows(CustomerNotFoundExceptions.class, () -> customerService.updateCustomer(requestMock));

        // Then
        verify(customerRepository, times(1)).findById(CUSTOMER_ID);
        verify(customerRepository, times(0)).save(existingCustomer); // explicit zero-call on known object
        verifyNoMoreInteractions(customerRepository);
    }


    @Test
    void findAllCustomers_ShouldReturnMappedResponses() {
        // Given
        final Customer existingCustomerOne =
                constructCustomer("cust_123", "test-user-firstname-123",
                        "test-user-lastname-123", "abc@gmail.com");
        final Customer existingCustomerTwo = constructCustomer("cust_456", "test-user-firstname-456",
                "test-user-lastname-456", "xyz@gmail.com");

        when(customerRepository.findAll()).thenReturn(List.of(existingCustomerOne, existingCustomerTwo));
        final CustomerResponse customerResponseOne = existingCustomerOne.fromCustomer();
        final CustomerResponse customerResponseTwo = existingCustomerTwo.fromCustomer();

        // When
        List<CustomerResponse> actual = customerService.findAllCustomers();

        // Then
        assertEquals(List.of(customerResponseOne, customerResponseTwo), actual);
    }


    @Test
    void findById_ShouldReturnResponse_WhenFound() throws CustomerNotFoundExceptions {
        // Given
        final Customer existingCustomer = constructCustomer(CUSTOMER_ID, FIRST_NAME, LAST_NAME, EMAIL_EXISTING);
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(existingCustomer));

        // When
        final CustomerResponse actual = customerService.findById(CUSTOMER_ID);
        final CustomerResponse expected = existingCustomer.fromCustomer();

        // Then
        assertEquals(expected, actual);
    }


    @Test
    void findById_ShouldThrow_WhenNotFound() {
        // Given
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());
        // Then
        assertThrows(CustomerNotFoundExceptions.class, () -> customerService.findById(CUSTOMER_ID));
        verify(customerRepository, times(1)).findById(CUSTOMER_ID);
    }


    @Test
    void findByEmail_ShouldReturnResponse_WhenFound() throws CustomerNotFoundExceptions {
        // Given
        final Customer existingCustomer = constructCustomer(CUSTOMER_ID, FIRST_NAME, LAST_NAME, EMAIL_EXISTING);
        when(customerRepository.findByEmail(EMAIL_EXISTING)).thenReturn(Optional.of(existingCustomer));

        // When
        final CustomerResponse actual = customerService.findByEmail(EMAIL_EXISTING);
        final CustomerResponse expected = existingCustomer.fromCustomer();

        // Then
        assertEquals(expected, actual);
    }


    @Test
    void findByEmail_ShouldThrow_WhenNotFound() {
        // Given
        when(customerRepository.findByEmail(EMAIL_NEW)).thenReturn(Optional.empty());
        // Then
        assertThrows(CustomerNotFoundExceptions.class, () -> customerService.findByEmail(EMAIL_NEW));
        verify(customerRepository, times(1)).findByEmail(EMAIL_NEW);
    }


    @Test
    void existsById_ShouldReturnTrue_WhenFound() {
        // Given
        final Customer existingCustomer = constructCustomer(CUSTOMER_ID, FIRST_NAME, LAST_NAME, EMAIL_EXISTING);
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(existingCustomer));
        // Then
        assertTrue(customerService.existsById(CUSTOMER_ID));
    }


    @Test
    void existsById_ShouldReturnFalse_WhenNotFound() {
        // Given
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());
        // When Then
        assertFalse(customerService.existsById(CUSTOMER_ID));
    }


    @Test
    void deleteCustomer_ShouldCallDeleteById_AndReturnMessage() {
        // When
        final String msg = customerService.deleteCustomer(CUSTOMER_ID);

        // Then
        verify(customerRepository, times(1)).deleteById(CUSTOMER_ID);
        assertTrue(msg.contains(CUSTOMER_ID));
    }


    private Customer constructCustomer(final String customerId,
                                       final String firstName,
                                       final String lastName,
                                       final String customerEmail
    ) {
        return Customer.builder()
                .customerId(customerId)
                .firstName(firstName)
                .lastName(lastName)
                .customerEmail(customerEmail)
                .address(constructAddress())
                .build();
    }

    private CustomerRequest constructCustomerRequest() {
        return CustomerRequest.builder()
                .id(CUSTOMER_ID)
                .firstname(FIRST_NAME)
                .lastname(LAST_NAME)
                .email(EMAIL_NEW)
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
}
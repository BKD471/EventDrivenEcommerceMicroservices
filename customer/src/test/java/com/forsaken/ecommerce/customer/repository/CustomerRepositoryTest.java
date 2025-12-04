package com.forsaken.ecommerce.customer.repository;


import com.forsaken.ecommerce.customer.configs.dynamodb.DynamoDbProperties;
import com.forsaken.ecommerce.customer.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerRepositoryTest {

    private static final String CUSTOMER_ID = "cust-123";
    private static final String CUSTOMER_EMAIL = "abc@gmail.com";

    private DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<Customer> customerTable;
    private DynamoDbIndex<Customer> emailIndex;
    private DynamoDbProperties properties;
    private CustomerRepository repository;

    @BeforeEach
    void setup() {
        enhancedClient = mock(DynamoDbEnhancedClient.class);
        customerTable = mock(DynamoDbTable.class);
        emailIndex = mock(DynamoDbIndex.class);
        properties = mock(DynamoDbProperties.class);

        when(properties.tableName()).thenReturn("customer-table");
        when(enhancedClient.table(eq("customer-table"), any(TableSchema.class)))
                .thenReturn(customerTable);

        repository = new CustomerRepository(enhancedClient, properties);
    }


    @Test
    void testSave() {
        // Given
        final Customer customer = constructCustomer(CUSTOMER_ID);

        // When
        repository.save(customer);

        // Then
        verify(customerTable, times(1)).putItem(customer);
    }


    @Test
    void testFindById_Found() {
        // Given
        final Customer mockCustomer = constructCustomer(CUSTOMER_ID);

        // When
        // Capture the consumer passed to getItem and stub getItem using the captor
        final ArgumentCaptor<Consumer> captor = ArgumentCaptor.forClass(Consumer.class);
        when(customerTable.getItem(captor.capture())).thenReturn(mockCustomer);
        final Optional<Customer> result = repository.findById(CUSTOMER_ID);

        // Then
        assertTrue(result.isPresent());
        assertEquals(CUSTOMER_ID, result.get().getCustomerId());
        // Additional verification: ensure a Consumer was passed into getItem (i.e. repository built the request)
        Consumer captured = captor.getValue();
        assertNotNull(captured);
    }

    @Test
    void testFindById_NotFound() {
        // When
        final ArgumentCaptor<Consumer> captor = ArgumentCaptor.forClass(Consumer.class);
        when(customerTable.getItem(captor.capture())).thenReturn(null);
        final Optional<Customer> result = repository.findById(CUSTOMER_ID);

        // Then
        assertFalse(result.isPresent());
        final Consumer captured = captor.getValue();
        assertNotNull(captured);
    }


    @Test
    void testFindAll() {
        // Given
        final Customer customerOne = constructCustomer("cust-123");
        final Customer customerTwo = constructCustomer("cust-456");

        // When
        // Mock Page
        final Page<Customer> page = mock(Page.class);
        when(page.items()).thenReturn(List.of(customerOne, customerTwo));

        // Mock PageIterable (actual return type from scan())
        final PageIterable<Customer> iterable = mock(PageIterable.class);
        // Mock iterator<Page<Customer>>
        final Iterator<Page<Customer>> it = mock(Iterator.class);
        when(it.hasNext()).thenReturn(true, false);
        when(it.next()).thenReturn(page);
        // When iterable.iterator() -> our mocked iterator
        when(iterable.iterator()).thenReturn(it);
        // scan() must return PageIterable<Customer>
        when(customerTable.scan()).thenReturn(iterable);
        final List<Customer> all = repository.findAll();

        // Then
        assertEquals(2, all.size());
        assertEquals("cust-123", all.get(0).getCustomerId());
        assertEquals("cust-456", all.get(1).getCustomerId());
    }


    @Test
    void testDeleteById() {
        // When
        final ArgumentCaptor<Consumer> captor = ArgumentCaptor.forClass(Consumer.class);
        repository.deleteById(CUSTOMER_ID);

        // Then
        // Verify deleteItem was called exactly once and capture the Consumer argument
        verify(customerTable, times(1)).deleteItem(captor.capture());
        // Verify repository actually supplied a Consumer
        final Consumer captured = captor.getValue();
        assertNotNull(captured);
    }


    @Test
    void testFindByEmail_Found() {
        // Given
        final Customer customer = constructCustomer(CUSTOMER_ID);

        // When
        when(customerTable.index("email-index")).thenReturn(emailIndex);
        final Page<Customer> page = mock(Page.class);
        when(page.items()).thenReturn(List.of(customer));

        final SdkIterable<Page<Customer>> iterable = mock(SdkIterable.class);
        when(iterable.iterator()).thenReturn(List.of(page).iterator());

        // Capture the Consumer passed into emailIndex.query(...)
        final ArgumentCaptor<Consumer> captor = ArgumentCaptor.forClass(Consumer.class);
        when(emailIndex.query(captor.capture())).thenReturn(iterable);
        final Optional<Customer> result = repository.findByEmail("abc@gmail.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals(CUSTOMER_ID, result.get().getCustomerId());
        // Verify repository actually supplied a Consumer
        assertNotNull(captor.getValue());
    }

    @Test
    void testFindByEmail_NotFound() {
        // When
        when(customerTable.index("email-index")).thenReturn(emailIndex);
        final Page<Customer> page = mock(Page.class);
        when(page.items()).thenReturn(List.of());

        final SdkIterable<Page<Customer>> iterable = mock(SdkIterable.class);
        when(iterable.iterator()).thenReturn(List.of(page).iterator());

        // Capture the Consumer passed into emailIndex.query(...) instead of using any(...)
        final ArgumentCaptor<Consumer> captor = ArgumentCaptor.forClass(Consumer.class);
        when(emailIndex.query(captor.capture())).thenReturn(iterable);
        final Optional<Customer> result = repository.findByEmail("xyz@gmail.com");

        // Then
        assertFalse(result.isPresent());
        // Verify repository actually supplied a Consumer
        assertNotNull(captor.getValue());
    }


    private Customer constructCustomer(final String customerId) {
        return Customer.builder().customerId(customerId).firstName("John").customerEmail(CUSTOMER_EMAIL).build();
    }
}


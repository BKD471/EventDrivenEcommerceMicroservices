package com.forsaken.ecommerce.customer.repository;

import com.forsaken.ecommerce.customer.configs.dynamodb.DynamoDbProperties;
import com.forsaken.ecommerce.customer.model.Customer;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepository {

    private final DynamoDbTable<Customer> customerTable;
    private final DynamoDbEnhancedClient client;
    private final DynamoDbProperties dynamoDbProperties;
    private final Class<?> className = Customer.class;

    public CustomerRepository(
            final DynamoDbEnhancedClient enhancedClient,
            final DynamoDbProperties dynamoDbProperties
    ) {
        this.dynamoDbProperties = dynamoDbProperties;
        this.client = enhancedClient;
        this.customerTable = client.table(dynamoDbProperties.tableName(), TableSchema.fromBean(Customer.class));
    }

    public void save(final Customer customer) {
        customerTable.putItem(customer);
    }

    public Optional<Customer> findById(final String customerId) {
        return Optional.ofNullable(customerTable.getItem(r -> r.key(k -> k.partitionValue(customerId))));
    }

    public List<Customer> findAll() {
        final List<Customer> customers = new ArrayList<>();
        final SdkIterable<Page<Customer>> pages = customerTable.scan();
        for (Page<Customer> page : pages) customers.addAll(page.items());
        return customers;
    }

    public void deleteById(final String customerId) {
        customerTable.deleteItem(r -> r.key(k -> k.partitionValue(customerId)));
    }

    public Optional<Customer> findByEmail(final String customerEmail) {
        final DynamoDbIndex<Customer> emailIndex = customerTable.index("email-index");
        SdkIterable<Page<Customer>> pages = emailIndex.query(r ->
                r.queryConditional(QueryConditional.keyEqualTo(k -> k.partitionValue(customerEmail)))
        );

        for (Page<Customer> page : pages) {
            for (Customer customer : page.items()) {
                return Optional.of(customer);
            }
        }
        return Optional.empty();
    }
}


package com.forsaken.ecommerce.customer.model;

import com.forsaken.ecommerce.customer.dto.CustomerResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;

@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    private String customerId;
    private String firstName;
    private String lastName;
    private String customerEmail;
    private Address address;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("customerId")
    public String getCustomerId() {
        return customerId;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "email-index")
    @DynamoDbAttribute("customerEmail")
    public String getCustomerEmail() {
        return customerEmail;
    }

    @DynamoDbAttribute("firstName")
    public String getFirstName() {
        return firstName;
    }

    @DynamoDbSecondarySortKey(indexNames = "lastName-index")
    @DynamoDbAttribute("lastName")
    public String getLastName() {
        return lastName;
    }

    @DynamoDbAttribute("address")
    public Address getAddress() {
        return address;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public CustomerResponse fromCustomer() {
        return new CustomerResponse(
                this.getCustomerId(),
                this.getFirstName(),
                this.getLastName(),
                this.getCustomerEmail(),
                this.getAddress()
        );
    }
}

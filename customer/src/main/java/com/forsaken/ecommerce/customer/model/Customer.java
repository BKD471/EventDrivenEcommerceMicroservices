package com.forsaken.ecommerce.customer.model;

import com.forsaken.ecommerce.customer.dto.CustomerResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Address address;


    public String getId() {
        return id;
    }


    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Address getAddress() {
        return address;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public CustomerResponse fromCustomer() {
        return new CustomerResponse(
                this.getId(),
                this.getFirstName(),
                this.getLastName(),
                this.getEmail(),
                this.getAddress()
        );
    }
}

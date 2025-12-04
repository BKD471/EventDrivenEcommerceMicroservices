package com.forsaken.ecommerce.customer.dto;

import com.forsaken.ecommerce.customer.model.Address;
import com.forsaken.ecommerce.customer.model.Customer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

@Validated
@Builder
public record CustomerRequest(
        String id,

        @NotNull(message = "Customer firstname is required")
        String firstname,

        @NotNull(message = "Customer firstname is required")
        String lastname,

        @NotNull(message = "Customer Email is required")
        @Email(message = "Customer Email is not a valid email address")
        String email,

        @NotNull(message = "Customer Address is required")
        Address address
) {

    public Customer toCustomer(final String id){
        return new Customer(
                id,
                this.firstname(),
                this.lastname(),
                this.email(),
                this.address()
        );
    }
}

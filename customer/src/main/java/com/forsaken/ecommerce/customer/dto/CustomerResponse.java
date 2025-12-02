package com.forsaken.ecommerce.customer.dto;

import com.forsaken.ecommerce.customer.model.Address;

public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        String email,
        Address address
) {

}

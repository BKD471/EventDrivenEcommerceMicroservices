package com.forsaken.ecommerce.customer.repository;

import com.forsaken.ecommerce.customer.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepository {

    public void save(final Customer customer) {
        // TODO save in db
    }

    public Optional<Customer> findById(final String customerId) {
        // TODO check in DB
        return Optional.empty();
    }


    public List<Customer> findAll() {
        // TODO fetch all customers
        return Collections.emptyList();
    }

    public void deleteById(final String customerId) {
        // TODO delete customer record by id
    }
}


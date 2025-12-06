package com.forsaken.ecommerce.product.repository;


import com.forsaken.ecommerce.product.model.Category;
import com.forsaken.ecommerce.product.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @Mock
    private IProductRepository repository;


    @Test
    void findAllByIdInOrderById_ShouldReturnProducts() {
        // Given
        final List<Integer> ids = List.of(1, 2, 3);
        final List<Product> products = List.of(new Product(), new Product());
        when(repository.findAllByIdInOrderById(ids)).thenReturn(products);

        // When
        List<Product> result = repository.findAllByIdInOrderById(ids);

        // Then
        assertEquals(products, result);
        verify(repository).findAllByIdInOrderById(ids);
    }


    @Test
    void findAllWithCategory_ShouldReturnPagedProducts() {
        // Given
        final Pageable pageable = PageRequest.of(0, 10);
        final List<Product> productList = List.of(new Product());
        final Page<Product> page = new PageImpl<>(productList, pageable, productList.size());
        when(repository.findAllWithCategory(pageable)).thenReturn(page);

        // When
        final Page<Product> result = repository.findAllWithCategory(pageable);

        // Then
        assertEquals(page, result);
        verify(repository).findAllWithCategory(pageable);
    }


    @Test
    void findAllByAdditionDateBetween_ShouldReturnProducts() {
        // Given
        final LocalDateTime from = LocalDateTime.now().minusDays(7);
        final LocalDateTime to = LocalDateTime.now();
        final List<Product> productList = List.of(new Product());
        when(repository.findAllByAdditionDateBetween(from, to)).thenReturn(productList);

        // When
        final List<Product> result = repository.findAllByAdditionDateBetween(from, to);

        // Then
        assertEquals(productList, result);
        verify(repository).findAllByAdditionDateBetween(from, to);
    }


    @Test
    void findAllByCategoryAndPriceGreaterThanEqual_ShouldReturnProducts() {
        // Given
        final Category category = new Category();
        final BigDecimal price = BigDecimal.valueOf(100);
        final List<Product> productList = List.of(new Product());
        when(repository.findAllByCategoryAndPriceGreaterThanEqual(category, price))
                .thenReturn(productList);

        // When
        final List<Product> result =
                repository.findAllByCategoryAndPriceGreaterThanEqual(category, price);

        // Then
        assertEquals(productList, result);
        verify(repository).findAllByCategoryAndPriceGreaterThanEqual(category, price);
    }


    @Test
    void findAllByCategoryAndPriceLessThanEqual_ShouldReturnProducts() {
        // Given
        final Category category = new Category();
        final BigDecimal price = BigDecimal.valueOf(100);
        final List<Product> productList = List.of(new Product());
        when(repository.findAllByCategoryAndPriceLessThanEqual(category, price))
                .thenReturn(productList);

        // When
        final List<Product> result =
                repository.findAllByCategoryAndPriceLessThanEqual(category, price);

        // Then
        assertEquals(productList, result);
        verify(repository).findAllByCategoryAndPriceLessThanEqual(category, price);
    }
}
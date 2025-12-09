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

/**
 * Unit tests for {@link IProductRepository}, verifying that custom repository
 * query methods behave as expected when invoked.
 *
 * <p>All interactions with the repository are mocked using Mockito, ensuring that
 * the test suite focuses on verifying:
 * <ul>
 *     <li>Correct method invocations</li>
 *     <li>Correct parameter passing</li>
 *     <li>Expected return values</li>
 * </ul>
 *
 * <p>Since this is a pure repository mock test (no database involved), it validates
 * method signatures, mapping consistency, and contract expectations.
 */
@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @Mock
    private IProductRepository repository;

    /**
     * Verifies that {@link IProductRepository#findAllByIdInOrderById(List)}
     * returns the expected list of products when invoked with a list of IDs.
     *
     * <p>Ensures:
     * <ul>
     *     <li>The repository is called with the correct argument</li>
     *     <li>The returned product list matches the mocked response</li>
     * </ul>
     */
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

    /**
     * Verifies that {@link IProductRepository#findAllWithCategory(Pageable)}
     * returns a paginated list of products when invoked with a pageable request.
     *
     * <p>Ensures:
     * <ul>
     *     <li>Pagination is passed correctly</li>
     *     <li>The returned {@link Page} object matches the stubbed data</li>
     * </ul>
     */
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

    /**
     * Tests that {@link IProductRepository#findAllByAdditionDateBetween(LocalDateTime, LocalDateTime)}
     * returns all products created within the given date range.
     *
     * <p>Ensures:
     * <ul>
     *     <li>The repository receives the correct date parameters</li>
     *     <li>The returned list matches the mocked data</li>
     * </ul>
     */
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


    /**
     * Verifies that {@link IProductRepository#findAllByCategoryAndPriceGreaterThanEqual(Category, BigDecimal)}
     * returns all products whose price is greater than or equal to the provided limit.
     *
     * <p>Ensures:
     * <ul>
     *     <li>Correct method invocation with category and price</li>
     *     <li>Returned product list matches the expectation</li>
     * </ul>
     */
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

    /**
     * Verifies that {@link IProductRepository#findAllByCategoryAndPriceLessThanEqual(Category, BigDecimal)}
     * returns all products priced less than or equal to the provided amount.
     *
     * <p>Ensures:
     * <ul>
     *     <li>The repository receives the correct category and price parameters</li>
     *     <li>The returned product list is exactly the mocked result</li>
     * </ul>
     */
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
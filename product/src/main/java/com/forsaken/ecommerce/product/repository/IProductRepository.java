package com.forsaken.ecommerce.product.repository;

import com.forsaken.ecommerce.product.model.Category;
import com.forsaken.ecommerce.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface IProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByIdInOrderById(final List<Integer> ids);
    @Query(
            value = "SELECT p FROM Product p JOIN FETCH p.category",
            countQuery = "SELECT count(p) FROM Product p"
    )
    Page<Product> findAllWithCategory(final Pageable pageable);
    List<Product> findAllByAdditionDateBetween(final LocalDateTime fromDate, final LocalDateTime toDate);
    List<Product> findAllByCategoryAndPriceGreaterThanEqual(final Category category, final BigDecimal price);
    List<Product> findAllByCategoryAndPriceLessThanEqual(final Category category, final BigDecimal price);
}

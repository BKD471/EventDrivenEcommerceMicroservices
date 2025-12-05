package com.forsaken.ecommerce.product.repository;

import com.forsaken.ecommerce.product.model.Category;
import com.forsaken.ecommerce.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface IProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByIdInOrderById(final List<Integer> ids);
    @Query("SELECT p FROM Product p JOIN FETCH p.category")
    List<Product> findAllWithCategory();
    List<Product> findAllByAdditionDateBetween(final LocalDateTime fromDate, final LocalDateTime toDate);
    List<Product> findAllByCategoryAndPriceGreaterThanEqual(final Category category, final BigDecimal price);
    List<Product> findAllByCategoryAndPriceLessThanEqual(final Category category, final BigDecimal price);
}

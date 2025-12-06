package com.forsaken.ecommerce.product.repository;

import com.forsaken.ecommerce.product.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<Category, Integer> {
}

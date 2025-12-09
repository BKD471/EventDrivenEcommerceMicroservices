package com.forsaken.ecommerce.product.repository;

import com.forsaken.ecommerce.product.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on {@link Category} entities.
 *
 * <p>This interface extends Spring Data JPA's {@link JpaRepository}, which provides a
 * complete set of standard persistence methods including save, update, delete, and
 * various query capabilities. No additional methods are defined here, but custom
 * queries may be added in the future as needed.</p>
 *
 * <p>Key operations inherited from {@link JpaRepository} include:</p>
 * <ul>
 *     <li>{@code save(Category entity)} – persists or updates a category</li>
 *     <li>{@code findById(Integer id)} – retrieves a category by its identifier</li>
 *     <li>{@code findAll()} – fetches all categories</li>
 *     <li>{@code deleteById(Integer id)} – removes a category by ID</li>
 *     <li>Pagination and sorting support through {@code findAll(Pageable pageable)}</li>
 * </ul>
 *
 * <p>This repository is typically used by service-layer components to interact with
 * category data stored in the underlying relational database.</p>
 */
public interface ICategoryRepository extends JpaRepository<Category, Integer> {
}

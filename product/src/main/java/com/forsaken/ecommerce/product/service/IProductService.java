package com.forsaken.ecommerce.product.service;

import com.forsaken.ecommerce.common.exceptions.ProductNotFoundExceptions;
import com.forsaken.ecommerce.product.dto.Direction;
import com.forsaken.ecommerce.product.dto.ProductPurchaseRequest;
import com.forsaken.ecommerce.product.dto.ProductPurchaseResponse;
import com.forsaken.ecommerce.product.dto.ProductRequest;
import com.forsaken.ecommerce.product.dto.ProductResponse;
import com.forsaken.ecommerce.product.model.Category;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IProductService {


    /**
     * this service creates Product in database.
     *
     * @param request - request to create Product
     * @return Integer - acknowledgment that Product has been created with ProductId
     */
    Integer createProduct(final ProductRequest request);


    /**
     * this service fetches all Products from database.
     *
     * @param signedUrls              - is SignedUrl or not.
     * @return List<ProductResponse>  - list of products from database.
     */
    List<ProductResponse> getAllProducts(final Boolean signedUrls);


    /**
     * this service fetches Product by id from database.
     *
     * @param id              - productId
     * @param signedUrl       - is SignedUrl or not.
     * @return Optional<ProductResponse>  - product information from database.
     */
    Optional<ProductResponse> getProductById(
            final Integer id,
            final boolean signedUrl
    );


    /**
     * this service purchases products from database.
     *
     * @param request                         - request to purchase Product
     * @return List<ProductPurchaseResponse>  - product information from database.
     */
    @Transactional(rollbackFor = ProductNotFoundExceptions.class)
    List<ProductPurchaseResponse> purchaseProducts(
            final List<ProductPurchaseRequest> request
    ) throws ProductNotFoundExceptions;


    /**
     * this service fetches products from database that was created between fromDate and toDate.
     *
     * @param fromDate                - fromDate where from search begins
     * @param toDate                  - fromDate where from search begins
     * @return List<ProductResponse>  - list of product information from database.
     */
    List<ProductResponse> findAllProducts(
            final LocalDateTime fromDate,
            final LocalDateTime toDate
    );

    /**
     * this service fetches products from database by category with price either less than equals or greater than equals.
     *
     * @param category                - category to filter
     * @param price                   - price to filter
     * @param direction               - whether pricing is less than equals or greater than equals
     * @return List<ProductResponse>  - list of product information from database.
     */
    List<ProductResponse> findAllProductsByCategory(
            final Category category,
            final BigDecimal price,
            final Direction direction
    );
}

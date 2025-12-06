package com.forsaken.ecommerce.product.service;

import com.forsaken.ecommerce.common.exceptions.ProductNotFoundExceptions;
import com.forsaken.ecommerce.product.dto.PagedResponse;
import com.forsaken.ecommerce.product.dto.ProductPurchaseRequest;
import com.forsaken.ecommerce.product.dto.ProductPurchaseResponse;
import com.forsaken.ecommerce.product.dto.ProductRequest;
import com.forsaken.ecommerce.product.dto.ProductResponse;
import com.forsaken.ecommerce.product.exceptions.CategoryNotFoundExceptions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.forsaken.ecommerce.product.dto.ProductRequest.Direction;

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
     * @param signedUrls - is signedUrl or not for viewing/downloading product image
     * @return PagedResponse<ProductResponse>  - page of products from database.
     */
    PagedResponse<ProductResponse> getAllProducts(
            final Boolean signedUrls,
            final int page,
            final int size
    );


    /**
     * this service fetches Product by id from database.
     *
     * @param id        - productId
     * @param signedUrl - is signedUrl or not for viewing/downloading product image
     * @return Optional<ProductResponse>  - product information from database.
     */
    Optional<ProductResponse> getProductById(
            final Integer id,
            final boolean signedUrl
    );


    /**
     * this service purchases products from database.
     *
     * @param request - request to purchase Product
     * @return PagedResponse<ProductPurchaseResponse>  - page of all product purchase information from database.
     */
    @Transactional(rollbackFor = ProductNotFoundExceptions.class)
    PagedResponse<ProductPurchaseResponse> purchaseProducts(
            final List<ProductPurchaseRequest> request,
            final int page,
            final int size
    ) throws ProductNotFoundExceptions;


    /**
     * this service fetches products from database that was created between fromDate and toDate.
     *
     * @param fromDate - fromDate where from search begins
     * @param toDate   - fromDate where from search begins
     * @param page     - index of page starts from 1
     * @param size     - no of elements per each page
     * @return PagedResponse<ProductResponse>  - page of all product information from database.
     */
    PagedResponse<ProductResponse> findAllProducts(
            final LocalDateTime fromDate,
            final LocalDateTime toDate,
            final int page,
            final int size
    );

    /**
     * this service fetches products from database by category with price either less than equals or greater than equals.
     *
     * @param categoryId - id of category of that product client needs
     * @param price      - price limit to which we search
     * @param direction  - direction of search, either less than equals price or greater than equals price
     * @param page       - index of page starts from 1
     * @param size       - no of elements per each page
     * @return List<ProductResponse>  - page of all product information that falls under this category and has price limit.
     */
    PagedResponse<ProductResponse> findAllProductsByCategory(
            final Integer categoryId,
            final BigDecimal price,
            final Direction direction,
            final int page,
            final int size
    ) throws CategoryNotFoundExceptions;
}

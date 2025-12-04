package com.forsaken.ecommerce.product.controller;

import com.forsaken.ecommerce.common.responses.ApiResponse;
import com.forsaken.ecommerce.product.dto.Direction;
import com.forsaken.ecommerce.product.dto.ProductPurchaseRequest;
import com.forsaken.ecommerce.product.dto.ProductRequest;
import com.forsaken.ecommerce.product.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class ProductControllerImpl implements IProductController {

    @Override
    public ResponseEntity<ApiResponse<?>> getPresignedUrl(final String fileName, final String contentType) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.builder()
                                .status(ApiResponse.Status.SUCCESS)
                                .data(null)
                                .message("Presigned Url Generated.")
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<?>> createProduct(final ProductRequest productRequest) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.builder()
                                .status(ApiResponse.Status.SUCCESS)
                                .data(null)
                                .message("Product Created")
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<?>> getDownloadUrl(final String key) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.builder()
                                .status(ApiResponse.Status.SUCCESS)
                                .data(null)
                                .message("Presigned Url to download Product Image Generated.")
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<?>> purchaseProducts(final List<ProductPurchaseRequest> request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(
                        ApiResponse.builder()
                                .status(ApiResponse.Status.SUCCESS)
                                .data(null)
                                .message("Product Purchased.")
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<?>> findById(final Integer productId, final Boolean signedUrl) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.builder()
                                .status(ApiResponse.Status.SUCCESS)
                                .data(null)
                                .message("Product Found with Id: " + productId)
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<?>> findAll(final Boolean signedUrl) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.builder()
                                .status(ApiResponse.Status.SUCCESS)
                                .data(null)
                                .message("Fetched All Products Information.")
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<?>> findAllProducts(LocalDateTime fromDate, LocalDateTime toDate) {
        if (toDate == null) toDate = LocalDateTime.now();
        if (fromDate == null) fromDate = toDate.minusMonths(6);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.builder()
                                .status(ApiResponse.Status.SUCCESS)
                                .data(null)
                                .message("Fetched All Products Information created between fromDate to toDate.")
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<?>> findAllProductsByCategory(
            final Category category,
            final BigDecimal price,
            final Direction direction
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.builder()
                                .status(ApiResponse.Status.SUCCESS)
                                .data(null)
                                .message("Fetched All Products By Category:category.")
                                .build()
                );
    }
}

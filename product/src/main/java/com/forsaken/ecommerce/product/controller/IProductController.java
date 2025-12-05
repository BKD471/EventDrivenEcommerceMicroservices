package com.forsaken.ecommerce.product.controller;

import com.forsaken.ecommerce.common.exceptions.ProductNotFoundExceptions;
import com.forsaken.ecommerce.common.responses.ApiResponse;
import com.forsaken.ecommerce.product.dto.Direction;
import com.forsaken.ecommerce.product.dto.ProductPurchaseRequest;
import com.forsaken.ecommerce.product.dto.ProductRequest;
import com.forsaken.ecommerce.product.model.Category;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@RequestMapping("/api/v1/products")
public interface IProductController {

    @GetMapping("/presigned-url")
    ResponseEntity<ApiResponse<?>> getPresignedUrl(
            @RequestParam @NotBlank final String fileName,
            @RequestParam @NotBlank final String contentType
    );


    @PostMapping(value = "/create")
    ResponseEntity<ApiResponse<?>> createProduct(
            @RequestBody @Valid final ProductRequest productRequest) throws IOException;


    @GetMapping("/download-url")
    ResponseEntity<ApiResponse<?>> getDownloadUrl(@NotBlank @RequestParam String key);


    @PostMapping("/purchase")
    ResponseEntity<ApiResponse<?>> purchaseProducts(
            @RequestBody @Valid final List<ProductPurchaseRequest> request
    ) throws ProductNotFoundExceptions;


    @GetMapping("/{product-id}")
    ResponseEntity<ApiResponse<?>> findById(
            @PathVariable("product-id") @NotNull final Integer productId,
            @RequestParam(name = "signedUrl",defaultValue = "True") final Boolean signedUrl
    );


    @GetMapping
    ResponseEntity<ApiResponse<?>> findAll(
            @RequestParam(name = "signedUrl",defaultValue = "True") final Boolean signedUrl
    );


    @GetMapping("/findAll")
    ResponseEntity<ApiResponse<?>> findAllProducts(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            final LocalDateTime fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            final LocalDateTime toDate
    );


    @GetMapping("/category")
    ResponseEntity<ApiResponse<?>> findAllProductsByCategory(
            @PathVariable("category") @Valid final Category category,
            @RequestParam(value = "price", defaultValue = "100") final BigDecimal price,
            @RequestParam(value = "direction", defaultValue = "GE") final Direction direction
    );
}

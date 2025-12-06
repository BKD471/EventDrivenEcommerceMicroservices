package com.forsaken.ecommerce.product.controller;

import com.forsaken.ecommerce.common.exceptions.ProductNotFoundExceptions;
import com.forsaken.ecommerce.common.responses.ApiResponse;
import com.forsaken.ecommerce.product.dto.PagedResponse;
import com.forsaken.ecommerce.product.dto.ProductPurchaseRequest;
import com.forsaken.ecommerce.product.dto.ProductPurchaseResponse;
import com.forsaken.ecommerce.product.dto.ProductRequest;
import com.forsaken.ecommerce.product.dto.ProductResponse;
import com.forsaken.ecommerce.product.exceptions.CategoryNotFoundExceptions;
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
import java.util.Map;

import static com.forsaken.ecommerce.product.dto.ProductRequest.Direction;


@RequestMapping("/api/v1/products")
public interface IProductController {

    /**
     * this service generates presigned url that will be used to upload or download file from S3.
     *
     * @param fileName    - name if file to be uploaded
     * @param contentType - type of file
     * @return ApiResponse<?> - acknowledgment that presigned url has been generated with status code.
     */
    @GetMapping("/presigned-url")
    ResponseEntity<ApiResponse<Map<String, String>>> getPresignedUrl(
            @RequestParam(name = "fileName") @NotBlank final String fileName,
            @RequestParam(name = "contentType") @NotBlank final String contentType
    );


    /**
     * this service creates product in database.
     *
     * @param productRequest - request to create product
     * @return ApiResponse<?> - acknowledgment that product has been created in database with status code.
     */
    @PostMapping(value = "/create")
    ResponseEntity<ApiResponse<Integer>> createProduct(
            @RequestBody @Valid final ProductRequest productRequest) throws IOException;


    /**
     * this service generates presigned url that will be used to upload or download file from S3..
     *
     * @param key - request to create product
     * @return ApiResponse<?> - url to download file from S3.
     */
    @GetMapping("/download-url")
    ResponseEntity<ApiResponse<String>> getDownloadUrl(@NotBlank @RequestParam(name = "key") final String key);


    /**
     * this service purchase products from database.
     *
     * @param request - request to purchase product
     * @param page    - index of page starts from 1
     * @param size    - no of elements per each page
     * @return ApiResponse<?> - acknowledgment that product has been purchased from database with status code.
     */
    @PostMapping("/purchase")
    ResponseEntity<ApiResponse<PagedResponse<ProductPurchaseResponse>>> purchaseProducts(
            @RequestBody @Valid final List<ProductPurchaseRequest> request,
            @RequestParam(name = "page", defaultValue = "1") final int page,
            @RequestParam(name = "size", defaultValue = "3") final int size
    ) throws ProductNotFoundExceptions;


    /**
     * this service fetches product information by id
     *
     * @param productId - id of product
     * @param signedUrl - is signedUrl or not for viewing/downloading product image
     * @return ApiResponse<?>   - product information with status code.
     */
    @GetMapping("/{product-id}")
    ResponseEntity<ApiResponse<ProductResponse>> findById(
            @PathVariable("product-id") @NotNull final Integer productId,
            @RequestParam(name = "signedUrl", defaultValue = "True") final Boolean signedUrl
    ) throws ProductNotFoundExceptions;


    /**
     * this service fetches all products from database.
     *
     * @param signedUrl - is signedUrl or not for viewing/downloading product image
     * @param page      - index of page starts from 1
     * @param size      - no of elements per each page
     * @return ApiResponse<?> - page of all product information with status code.
     */
    @GetMapping
    ResponseEntity<ApiResponse<PagedResponse<ProductResponse>>> findAll(
            @RequestParam(name = "signedUrl", defaultValue = "True") final Boolean signedUrl,
            @RequestParam(name = "page", defaultValue = "1") final int page,
            @RequestParam(name = "size", defaultValue = "3") final int size
    );


    /**
     * this service fetches all products from database that created between fromDate & endDate.
     *
     * @param fromDate - beginning point of search when product was created
     * @param toDate   - ending point of search when product was created
     * @param page     - index of page starts from 1
     * @param size     - no of elements per each page
     * @return ApiResponse<?> - page of all product information with status code.
     */
    @GetMapping("/findAll")
    ResponseEntity<ApiResponse<PagedResponse<ProductResponse>>> findAllProducts(
            @RequestParam(name = "fromDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime fromDate,

            @RequestParam(name = "toDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime toDate,

            @RequestParam(name = "page", defaultValue = "1") final int page,
            @RequestParam(name = "size", defaultValue = "3") final int size
    );


    /**
     * this service fetches all products from database that has the category and price limit wised by client..
     *
     * @param categoryId - id of category of that product client needs
     * @param price      - price limit to which we search
     * @param direction  - direction of search, either less than equals price or greater than equals price
     * @param page       - index of page starts from 1
     * @param size       - no of elements per each page
     * @return ApiResponse<?> - page of all product information that falls under this category and has price limit with status code.
     */
    @GetMapping("/category/{categoryId}")
    ResponseEntity<ApiResponse<PagedResponse<ProductResponse>>> findAllProductsByCategory(
            @PathVariable("categoryId") @NotNull final Integer categoryId,
            @RequestParam(value = "price", defaultValue = "100") final BigDecimal price,
            @RequestParam(value = "direction", defaultValue = "GE") final Direction direction,
            @RequestParam(name = "page", defaultValue = "1") final int page,
            @RequestParam(name = "size", defaultValue = "3") final int size
    ) throws CategoryNotFoundExceptions;
}

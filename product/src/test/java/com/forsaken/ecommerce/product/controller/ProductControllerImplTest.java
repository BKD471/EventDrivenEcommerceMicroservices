package com.forsaken.ecommerce.product.controller;


import com.forsaken.ecommerce.common.exceptions.ProductNotFoundExceptions;
import com.forsaken.ecommerce.common.responses.ApiResponse;
import com.forsaken.ecommerce.product.dto.PagedResponse;
import com.forsaken.ecommerce.product.dto.ProductPurchaseRequest;
import com.forsaken.ecommerce.product.dto.ProductPurchaseResponse;
import com.forsaken.ecommerce.product.dto.ProductRequest;
import com.forsaken.ecommerce.product.dto.ProductResponse;
import com.forsaken.ecommerce.product.exceptions.CategoryNotFoundExceptions;
import com.forsaken.ecommerce.product.service.IProductService;
import com.forsaken.ecommerce.product.service.IS3Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static com.forsaken.ecommerce.product.dto.ProductRequest.Direction;

@ExtendWith(MockitoExtension.class)
class ProductControllerImplTest {

    @Mock
    private IProductService service;

    @Mock
    private IS3Service s3Service;

    @InjectMocks
    private ProductControllerImpl controller;

    @Mock
    private ProductRequest productRequest;

    @Mock
    private ProductPurchaseRequest productPurchaseRequest;


    @Test
    void getPresignedUrl_ShouldReturnCreatedWithUrlMap() {
        // given
        final String fileName = "myFile.png";
        final String contentType = "image/png";
        final Map<String, String> urlMap = Map.of("url", "https://upload-url");

        when(s3Service.generatePresignedUploadUrl(fileName, contentType))
                .thenReturn(urlMap);

        // when
        ResponseEntity<ApiResponse<Map<String, String>>> response =
                controller.getPresignedUrl(fileName, contentType);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body);

        assertEquals(ApiResponse.Status.SUCCESS, body.status());
        assertEquals(urlMap, body.data());
        assertEquals("Presigned Url Generated.", body.message());

        verify(s3Service).generatePresignedUploadUrl(fileName, contentType);
        verifyNoInteractions(service);
    }


    @Test
    void createProduct_ShouldReturnCreatedWithProductId() throws IOException {
        // given
        final Integer generatedId = 101;
        when(service.createProduct(productRequest)).thenReturn(generatedId);

        // when
        final ResponseEntity<ApiResponse<Integer>> response =
                controller.createProduct(productRequest);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body);

        assertEquals(ApiResponse.Status.SUCCESS, body.status());
        assertEquals(generatedId, body.data());
        assertEquals("Product Created", body.message());
        verify(service).createProduct(productRequest);
        verifyNoInteractions(s3Service);
    }


    @Test
    void getDownloadUrl_ShouldReturnCreatedWithStringUrl() {
        // given
        final String key = "products/123.png";
        final String url = "https://download-url";

        when(s3Service.generatePresignedDownloadUrl(key)).thenReturn(url);

        // when
        final ResponseEntity<ApiResponse<String>> response =
                controller.getDownloadUrl(key);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals(url, body.data());
        assertEquals("Presigned Url to download Product Image Generated.", body.message());
        verify(s3Service).generatePresignedDownloadUrl(key);
        verifyNoInteractions(service);
    }


    @Test
    void purchaseProducts_ShouldReturnAcceptedWithPagedResponse() throws ProductNotFoundExceptions {
        // given
        final List<ProductPurchaseRequest> req = List.of(productPurchaseRequest);
        final int page = 0, size = 10;

        findAll_ShouldReturnOkWithPagedResponse();PagedResponse<ProductPurchaseResponse> paged =
                new PagedResponse<>(List.of(), 0, 10, 1,1);

        when(service.purchaseProducts(req, page, size)).thenReturn(paged);

        // when
        final ResponseEntity<ApiResponse<PagedResponse<ProductPurchaseResponse>>> response =
                controller.purchaseProducts(req, page, size);

        // then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals(paged, body.data());
        assertEquals("Product Purchased.", body.message());
        verify(service).purchaseProducts(req, page, size);
        verifyNoInteractions(s3Service);
    }


    @Test
    void findById_ShouldReturnOkWithProductResponse() throws ProductNotFoundExceptions {
        // given
        final Integer productId = 99;
        final Boolean signedUrl = true;

        findAll_ShouldReturnOkWithPagedResponse();ProductResponse productResponse =
                new ProductResponse(
                        99,
                        "Test Product",
                        "Test Description",
                        10.0,
                        BigDecimal.valueOf(199.99),
                        1,
                        "Category Name",
                        "Category Description",
                        "http://image-url"
                );

        when(service.getProductById(productId, signedUrl))
                .thenReturn(productResponse);

        // when
        final ResponseEntity<ApiResponse<ProductResponse>> response =
                controller.findById(productId, signedUrl);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals(productResponse, body.data());
        assertEquals("Product Found with Id: " + productId, body.message());
        verify(service).getProductById(productId, signedUrl);
        verifyNoInteractions(s3Service);
    }


    @Test
    void findAll_ShouldReturnOkWithPagedResponse() {
        // given
        final Boolean signedUrl = false;
        final int page = 0, size = 20;
        final PagedResponse<ProductResponse> paged =
                new PagedResponse<>(List.of(), 0, 20, 1,1);
        when(service.getAllProducts(signedUrl, page, size))
                .thenReturn(paged);

        // when
        final ResponseEntity<ApiResponse<PagedResponse<ProductResponse>>> response =
                controller.findAll(signedUrl, page, size);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals(paged, body.data());
        assertEquals("Fetched All Products Information.", body.message());
        verify(service).getAllProducts(signedUrl, page, size);
        verifyNoInteractions(s3Service);
    }


    @Test
    void findAllProducts_ShouldReturnOkWithPagedResponse() {
        // given
        final LocalDateTime from = LocalDateTime.now().minusDays(7);
        final LocalDateTime to = LocalDateTime.now();
        final int page = 0, size = 10;
        final PagedResponse<ProductResponse> paged =
                new PagedResponse<>(List.of(), 0, 10, 1,1);
        when(service.findAllProducts(from, to, page, size))
                .thenReturn(paged);

        // when
        final ResponseEntity<ApiResponse<PagedResponse<ProductResponse>>> response =
                controller.findAllProducts(from, to, page, size);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals(paged, body.data());
        assertEquals("Fetched All Products Information created between fromDate to toDate.", body.message());
        verify(service).findAllProducts(from, to, page, size);
        verifyNoInteractions(s3Service);
    }


    @Test
    void findAllProductsByCategory_ShouldReturnOkWithPaged() throws CategoryNotFoundExceptions {
        // given
        final Integer categoryId = 5;
        final BigDecimal price = BigDecimal.valueOf(200);
        final Direction direction = Direction.GE;
        final int page = 0, size = 10;
        final PagedResponse<ProductResponse> paged =
                new PagedResponse<>(List.of(), 0, 10, 1,1);
        when(service.findAllProductsByCategory(categoryId, price, direction, page, size))
                .thenReturn(paged);

        // when
        final ResponseEntity<ApiResponse<PagedResponse<ProductResponse>>> response =
                controller.findAllProductsByCategory(categoryId, price, direction, page, size);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals(paged, body.data());
        assertEquals("Fetched All Products By Category:category.", body.message());
        verify(service).findAllProductsByCategory(categoryId, price, direction, page, size);
        verifyNoInteractions(s3Service);
    }
}

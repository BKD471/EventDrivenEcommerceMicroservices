package com.forsaken.ecommerce.product.service;


import com.forsaken.ecommerce.common.exceptions.ProductNotFoundExceptions;
import com.forsaken.ecommerce.product.dto.PagedResponse;
import com.forsaken.ecommerce.product.dto.ProductPurchaseRequest;
import com.forsaken.ecommerce.product.dto.ProductPurchaseResponse;
import com.forsaken.ecommerce.product.dto.ProductRequest;
import com.forsaken.ecommerce.product.dto.ProductResponse;
import com.forsaken.ecommerce.product.exceptions.CategoryNotFoundExceptions;
import com.forsaken.ecommerce.product.model.Category;
import com.forsaken.ecommerce.product.model.Product;
import com.forsaken.ecommerce.product.repository.ICategoryRepository;
import com.forsaken.ecommerce.product.repository.IProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static com.forsaken.ecommerce.product.dto.ProductRequest.Direction;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private ICategoryRepository categoryRepository;

    @Mock
    private IS3Service s3Service;

    @InjectMocks
    private ProductServiceImpl service;


    @Test
    void createProduct_ShouldSaveAndReturnId() {
        // Given
        final ProductRequest request = mock(ProductRequest.class);
        final Product product = constructProduct();
        when(request.toProduct()).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        // When
        final Integer id = service.createProduct(request);

        // Then
        assertEquals(1, id);
        verify(productRepository).save(product);
    }


    @Test
    void getAllProducts_ShouldReturnSignedUrls_WhenEnabled() {
        // Given
        final Pageable pageable = PageRequest.of(0, 10);
        final Product product = constructProduct();
        final Page<Product> page = new PageImpl<>(List.of(product), pageable, 1);
        when(productRepository.findAllWithCategory(pageable))
                .thenReturn(page);
        when(s3Service.generatePresignedDownloadUrl("image-key"))
                .thenReturn("signed-url");

        // When
        final PagedResponse<ProductResponse> response =
                service.getAllProducts(true, 1, 10);

        // Then
        assertEquals(1, response.totalElements());
        assertEquals("signed-url", page.getContent().get(0).getImageUrl());
        verify(s3Service).generatePresignedDownloadUrl("image-key");
    }


    @Test
    void getProductById_ShouldReturnResponse_WhenProductExists() throws ProductNotFoundExceptions {
        // Given
        final Product product = constructProduct();
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(s3Service.generatePresignedDownloadUrl("image-key"))
                .thenReturn("signed-img");

        // When
        final ProductResponse response = service.getProductById(1, true);

        // Then
        assertEquals(1, response.id());
        assertEquals("signed-img", product.getImageUrl());
        verify(s3Service).generatePresignedDownloadUrl("image-key");
    }


    @Test
    void getProductById_ShouldThrow_WhenNotFound() {
        // Given
        when(productRepository.findById(10)).thenReturn(Optional.empty());

        // Then
        assertThrows(ProductNotFoundExceptions.class,
                () -> service.getProductById(10, false));
    }


    @Test
    void purchaseProducts_ShouldThrow_WhenMismatchInIds() {
        // Given
        final ProductPurchaseRequest productPurchaseRequest =
                new ProductPurchaseRequest(1, 1);
        when(productRepository.findAllByIdInOrderById(List.of(1)))
                .thenReturn(List.of());

        // Then
        assertThrows(ProductNotFoundExceptions.class,
                () -> service.purchaseProducts(List.of(productPurchaseRequest), 1, 10));
    }


    @Test
    void purchaseProducts_ShouldThrow_WhenStockInsufficient() {
        // Given
        final ProductPurchaseRequest productPurchaseRequest =
                new ProductPurchaseRequest(1, 10);
        final Product product = constructProduct();
        // Ensure insufficient stock
        product.setAvailableQuantity(5);
        when(productRepository.findAllByIdInOrderById(List.of(1)))
                .thenReturn(List.of(product));

        // Then
        assertThrows(ProductNotFoundExceptions.class,
                () -> service.purchaseProducts(List.of(productPurchaseRequest), 1, 10));
    }


    @Test
    void purchaseProducts_ShouldReturnPagedResponse_WhenSuccessful() throws ProductNotFoundExceptions {
        // Given
        final ProductPurchaseRequest productPurchaseRequest =
                new ProductPurchaseRequest(1, 2);
        final Product product = constructProduct();
        product.setId(1);
        product.setAvailableQuantity(5); // VERY IMPORTANT → matches your expected result
        when(productRepository.findAllByIdInOrderById(List.of(1)))
                .thenReturn(List.of(product));
        when(productRepository.save(product)).thenReturn(product);

        // When
        final PagedResponse<ProductPurchaseResponse> response =
                service.purchaseProducts(List.of(productPurchaseRequest), 1, 10);

        // Then
        assertEquals(1, response.totalElements());
        assertEquals(3, product.getAvailableQuantity());
    }


    @Test
    void findAllProducts_ShouldReturnPagedResponse() {
        // Given
        final Product product = constructProduct();

        // Create the exact timestamps to use
        final LocalDateTime fromDate = LocalDateTime.now().minusDays(1);
        final LocalDateTime toDate = LocalDateTime.now();

        // Stub using EXACT arguments (no any())
        when(productRepository.findAllByAdditionDateBetween(fromDate, toDate))
                .thenReturn(List.of(product));

        // When
        final PagedResponse<ProductResponse> response =
                service.findAllProducts(fromDate, toDate, 1, 10);

        // Then
        assertEquals(1, response.totalElements());
    }


    @Test
    void findAllProductsByCategory_ShouldThrow_WhenCategoryMissing() {
        // Given
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        // Then
        assertThrows(CategoryNotFoundExceptions.class, () ->
                service.findAllProductsByCategory(1, BigDecimal.TEN, Direction.GE, 1, 10)
        );
    }


    @Test
    void findAllProductsByCategory_ShouldReturnPaged_GreaterThanEqual() throws CategoryNotFoundExceptions {
        // Given
        final Category category = constructCategory();
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        final Product product = constructProduct();
        when(productRepository.findAllByCategoryAndPriceGreaterThanEqual(category, BigDecimal.TEN))
                .thenReturn(List.of(product));

        // When
        final PagedResponse<ProductResponse> response =
                service.findAllProductsByCategory(1, BigDecimal.TEN, Direction.GE, 1, 10);

        // Then
        assertEquals(1, response.totalElements());
        assertEquals(1, response.content().size());

        ProductResponse dto = response.content().get(0);
        assertEquals(product.getId(), dto.id());
        assertEquals(product.getName(), dto.name());
        assertEquals(category.getId(), dto.categoryId());
    }


    @Test
    void findAllProductsByCategory_ShouldReturnPaged_LessThanEqual() throws CategoryNotFoundExceptions {
        // Given
        final Category category = constructCategory();
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        final Product product = constructProduct();
        when(productRepository.findAllByCategoryAndPriceLessThanEqual(category, BigDecimal.TEN))
                .thenReturn(List.of(product));

        // When
        final PagedResponse<ProductResponse> response =
                service.findAllProductsByCategory(1, BigDecimal.TEN, Direction.LE, 1, 10);

        // Then
        assertEquals(1, response.totalElements());
    }

    private Product constructProduct() {
        return Product.builder()
                .id(1)
                .name("Test Product")
                .description("Test description")
                .price(BigDecimal.TEN)
                .category(constructCategory())
                .availableQuantity(20)
                .imageUrl("image-key")
                .build();
    }

    private Category constructCategory() {
        return Category.builder()
                .id(1)
                .name("Test Category")
                .build();
    }
}

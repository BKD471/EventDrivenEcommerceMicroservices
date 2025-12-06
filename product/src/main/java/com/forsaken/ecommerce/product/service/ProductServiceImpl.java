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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.forsaken.ecommerce.product.dto.ProductRequest.Direction;
import static com.forsaken.ecommerce.product.dto.ProductRequest.Direction.GE;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final IProductRepository repository;
    private final ICategoryRepository categoryRepository;
    private final IS3Service s3Service;
    private final Class<?> className = ProductServiceImpl.class;

    @Override
    public Integer createProduct(final ProductRequest request) {
        log.info("Received request to create product {}", request);
        final Product product = request.toProduct();
        return repository.save(product).getId();
    }

    @Override
    public PagedResponse<ProductResponse> getAllProducts(final Boolean signedUrls, final int page, final int size) {
        final Pageable pageable = PageRequest.of(page <= 1 ? 0 : page - 1, size);
        final Page<Product> productPage = repository.findAllWithCategory(pageable);


        if (signedUrls && productPage.hasContent())
            productPage.forEach(p -> p.setImageUrl(s3Service.generatePresignedDownloadUrl(p.getImageUrl())));

        final List<ProductResponse> content = productPage.getContent()
                .stream()
                .map(Product::toProductResponse)
                .toList();

        return PagedResponse.<ProductResponse>builder()
                .content(content)
                .page(productPage.getNumber() + 1)
                .size(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .build();
    }

    @Override
    public Optional<ProductResponse> getProductById(final Integer id, final boolean signedUrl) {
        log.info("Received request to get product by ID {}", id);
        final Optional<Product> productOpt = repository.findById(id);
        if (productOpt.isPresent() && signedUrl) {
            Product p = productOpt.get();
            p.setImageUrl(s3Service.generatePresignedDownloadUrl(p.getImageUrl()));
        }
        return Optional.ofNullable(productOpt.map(Product::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID:: " + id)));
    }

    @Override
    public PagedResponse<ProductPurchaseResponse> purchaseProducts(
            final List<ProductPurchaseRequest> request,
            final int page,
            final int size
    ) throws ProductNotFoundExceptions {

        log.info("Received request to purchase products {}", request);
        final var productIds = request
                .stream()
                .map(ProductPurchaseRequest::productId)
                .toList();
        final var storedProducts = repository.findAllByIdInOrderById(productIds);
        if (productIds.size() != storedProducts.size()) {
            throw new ProductNotFoundExceptions("One or more products does not exist",
                    "purchaseProducts(List<ProductPurchaseRequest> request) in " + className);
        }
        final var sortedRequest = request
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();
        final var purchasedProducts = new ArrayList<ProductPurchaseResponse>();
        for (int i = 0; i < storedProducts.size(); i++) {
            final var product = storedProducts.get(i);
            final var productRequest = sortedRequest.get(i);
            if (product.getAvailableQuantity() < productRequest.quantity()) {
                throw new ProductNotFoundExceptions("Insufficient stock quantity for product with ID:: " + productRequest.productId(),
                        "purchaseProducts(List<ProductPurchaseRequest> request) in " + className);
            }
            final var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);
            repository.save(product);
            purchasedProducts.add(product.toproductPurchaseResponse(productRequest.quantity()));
        }

        final int finalPage = Math.max(page - 1, 0);
        final int start = finalPage * size;
        final int end = Math.min(start + size, purchasedProducts.size());

        final List<ProductPurchaseResponse> pagedContent =
                (start >= purchasedProducts.size()) ? List.of() : purchasedProducts.subList(start, end);
        final int totalPages = (int) Math.ceil((double) purchasedProducts.size() / size);
        return PagedResponse.<ProductPurchaseResponse>builder()
                .content(pagedContent)
                .page(finalPage + 1)
                .size(size)
                .totalElements(purchasedProducts.size())
                .totalPages(totalPages)
                .build();
    }

    @Override
    public PagedResponse<ProductResponse> findAllProducts(LocalDateTime fromDate,
                                                          LocalDateTime toDate,
                                                          final int page,
                                                          final int size
    ) {
        log.info("Received request to get all products by date {} to {}", fromDate, toDate);

        if (toDate == null) toDate = LocalDateTime.now();
        if (fromDate == null) fromDate = toDate.minusMonths(6);
        final List<ProductResponse> responses = repository.findAllByAdditionDateBetween(fromDate, toDate)
                .stream()
                .map(Product::toProductResponse)
                .toList();

        final int finalPage = Math.max(page - 1, 0);
        final int start = finalPage * size;
        final int end = Math.min(start + size, responses.size());

        final List<ProductResponse> pagedContent =
                (start >= responses.size()) ? List.of() : responses.subList(start, end);
        final int totalPages = (int) Math.ceil((double) responses.size() / size);
        return PagedResponse.<ProductResponse>builder()
                .content(pagedContent)
                .page(finalPage + 1)
                .size(size)
                .totalElements(responses.size())
                .totalPages(totalPages)
                .build();
    }

    @Override
    public PagedResponse<ProductResponse> findAllProductsByCategory(
            final Integer categoryId,
            final BigDecimal price,
            final Direction direction,
            int page,
            int size) throws CategoryNotFoundExceptions {
        log.info("Received request to get all products by category {}", categoryId);

        final Category category = categoryRepository.findById(categoryId)
                .orElseThrow(
                        () -> new CategoryNotFoundExceptions(
                                "No Category found with ID: " + categoryId,
                                "findAllProductsByCategory(Integer categoryId,BigDecimal price," +
                                        "Direction direction,int page,int size) in " + className)
                );

        List<ProductResponse> products = null;
        if (GE.equals(direction)) {
            products = repository.findAllByCategoryAndPriceGreaterThanEqual(category, price)
                    .stream().map(Product::toProductResponse).collect(Collectors.toList());
        } else {
            products = repository.findAllByCategoryAndPriceLessThanEqual(category, price)
                    .stream().map(Product::toProductResponse).collect(Collectors.toList());
        }

        final int finalPage = Math.max(page - 1, 0);
        final int start = finalPage * size;
        final int end = Math.min(start + size, products.size());

        final List<ProductResponse> pagedContent =
                (start >= products.size()) ? List.of() : products.subList(start, end);
        final int totalPages = (int) Math.ceil((double) products.size() / size);
        return PagedResponse.<ProductResponse>builder()
                .content(pagedContent)
                .page(finalPage + 1)
                .size(size)
                .totalElements(products.size())
                .totalPages(totalPages)
                .build();
    }
}

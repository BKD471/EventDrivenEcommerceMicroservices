package com.forsaken.ecommerce.product.service;

import com.forsaken.ecommerce.common.exceptions.ProductNotFoundExceptions;
import com.forsaken.ecommerce.product.dto.Direction;
import com.forsaken.ecommerce.product.dto.ProductPurchaseRequest;
import com.forsaken.ecommerce.product.dto.ProductPurchaseResponse;
import com.forsaken.ecommerce.product.dto.ProductRequest;
import com.forsaken.ecommerce.product.dto.ProductResponse;
import com.forsaken.ecommerce.product.model.Category;
import com.forsaken.ecommerce.product.model.Product;
import com.forsaken.ecommerce.product.repository.IProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final IProductRepository repository;
    private final IS3Service s3Service;
    private final Class<?> className = ProductServiceImpl.class;

    @Override
    public Integer createProduct(final ProductRequest request) {
        log.info("Received request to create product {}", request);
        final Product product = request.toProduct();
        return repository.save(product).getId();
    }

    @Override
    public List<ProductResponse> getAllProducts(final Boolean signedUrls) {
        log.info("Received request to get all products {}", signedUrls);
        final List<Product> products = repository.findAllWithCategory();
        if (signedUrls) products.forEach(p -> p.setImageUrl(s3Service.generatePresignedDownloadUrl(p.getImageUrl())));
        return products.stream().map(Product::toProductResponse).collect(Collectors.toList());
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
    public List<ProductPurchaseResponse> purchaseProducts(final List<ProductPurchaseRequest> request) throws ProductNotFoundExceptions {
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
        return purchasedProducts;
    }

    @Override
    public List<ProductResponse> findAllProducts(LocalDateTime fromDate, LocalDateTime toDate) {
        log.info("Received request to get all products by date {}", fromDate);
        return repository.findAllByAdditionDateBetween(fromDate, toDate)
                .stream().map(Product::toProductResponse).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> findAllProductsByCategory(Category category, BigDecimal price, Direction direction) {
        log.info("Received request to get all products by category {}", category);
        if (Direction.GE.equals(direction)) {
            return repository.findAllByCategoryAndPriceGreaterThanEqual(category, price)
                    .stream().map(Product::toProductResponse).collect(Collectors.toList());
        } else {
            return repository.findAllByCategoryAndPriceLessThanEqual(category, price)
                    .stream().map(Product::toProductResponse).collect(Collectors.toList());
        }
    }
}

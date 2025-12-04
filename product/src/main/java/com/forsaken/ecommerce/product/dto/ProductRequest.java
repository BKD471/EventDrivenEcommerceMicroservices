package com.forsaken.ecommerce.product.dto;

import com.forsaken.ecommerce.product.model.Category;
import com.forsaken.ecommerce.product.model.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Validated
@Builder
public record ProductRequest(

        Integer id,

        @NotNull(message = "Product name is required")
        String name,

        @NotNull(message = "Product description is required")
        String description,

        @Positive(message = "Available quantity should be positive")
        double availableQuantity,

        @Positive(message = "Price should be positive")
        BigDecimal price,

        @NotNull(message = "Product category is required")
        Integer categoryId,

        String imageUrl
) {

    public Product toProduct() {
        return Product.builder()
                .id(this.id())
                .name(this.name())
                .description(this.description())
                .availableQuantity(this.availableQuantity())
                .price(this.price())
                .imageUrl(this.imageUrl())
                .category(
                        Category.builder()
                                .id(this.categoryId())
                                .build()
                )
                .build();
    }
}

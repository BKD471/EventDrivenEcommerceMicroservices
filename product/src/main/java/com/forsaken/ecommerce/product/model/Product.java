package com.forsaken.ecommerce.product.model;


import com.forsaken.ecommerce.product.dto.ProductPurchaseResponse;
import com.forsaken.ecommerce.product.dto.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Product {

    private Integer id;

    private String name;
    private String description;
    private double availableQuantity;
    private BigDecimal price;
    private LocalDateTime additionDate;
    private String imageUrl;
    private Category category;

    public ProductResponse toProductResponse() {
        return new ProductResponse(
                this.getId(),
                this.getName(),
                this.getDescription(),
                this.getAvailableQuantity(),
                this.getPrice(),
                this.getCategory().getId(),
                this.getCategory().getName(),
                this.getCategory().getDescription(),
                this.getImageUrl()
        );
    }

    public ProductPurchaseResponse toproductPurchaseResponse(double quantity) {
        return new ProductPurchaseResponse(
                this.getId(),
                this.getName(),
                this.getDescription(),
                this.getPrice(),
                quantity
        );
    }
}

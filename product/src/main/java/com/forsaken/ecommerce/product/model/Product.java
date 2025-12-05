package com.forsaken.ecommerce.product.model;


import com.forsaken.ecommerce.product.dto.ProductPurchaseResponse;
import com.forsaken.ecommerce.product.dto.ProductResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity
public class Product {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String description;
    private double availableQuantity;
    private BigDecimal price;
    @Column(name = "addition_date", nullable = false)
    private LocalDateTime additionDate;
    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
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

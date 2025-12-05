package com.forsaken.ecommerce.product.configs.aurora;


import lombok.Builder;

@Builder
public record AwsDbCredentials(
        String userName,
        String password,
        String host,
        String port,
        String dbName
) {
}
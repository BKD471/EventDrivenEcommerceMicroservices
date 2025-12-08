package com.forsaken.ecommerce.payment.configs.aurora;


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
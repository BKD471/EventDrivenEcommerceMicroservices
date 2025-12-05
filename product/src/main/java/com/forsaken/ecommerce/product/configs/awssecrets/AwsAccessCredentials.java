package com.forsaken.ecommerce.product.configs.awssecrets;

import lombok.Builder;

@Builder
public record AwsAccessCredentials(
        String accessKeyId,
        String secretKey,
        String region
) {

}
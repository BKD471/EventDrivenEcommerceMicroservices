package com.forsaken.ecommerce.customer.configs.awssecrets;

import lombok.Builder;

@Builder
public record AwsCredentials(
        String accessKeyId,
        String secretKey,
        String region
) {

}


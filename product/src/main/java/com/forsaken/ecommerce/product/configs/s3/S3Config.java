package com.forsaken.ecommerce.product.configs.s3;

import com.forsaken.ecommerce.product.configs.awssecrets.AwsAccessCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;


@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final AwsAccessCredentials accessCredentials;

    @Bean
    public S3Client s3Client() {
        final String region = accessCredentials.region();

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider())
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        final String region = accessCredentials.region();

        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider())
                .build();
    }

    private AwsCredentialsProvider credentialsProvider() {
        final String accessKeyId = accessCredentials.accessKeyId();
        final String secretAccessKey = accessCredentials.secretKey();

        if (accessKeyId != null && !accessKeyId.isBlank() &&
                secretAccessKey != null && !secretAccessKey.isBlank()) {
            return StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                            accessKeyId,
                            secretAccessKey
                    )
            );
        }
        // Fallback: use environment or IAM role
        return DefaultCredentialsProvider.create();
    }
}

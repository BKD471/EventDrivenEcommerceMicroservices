package com.forsaken.ecommerce.customer.configs.dynamodb;

import com.forsaken.ecommerce.customer.configs.awssecrets.AwsCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
@RequiredArgsConstructor
public class DynamoDBConfig {

    private final AwsCredentials awsCredentials;

    @Bean
    @Primary
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.of(awsCredentials.region()))
                .credentialsProvider(credentialsProvider())
                .build();
    }

    @Bean
    @Primary
    public DynamoDbEnhancedClient dynamoDbEnhancedClient() {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient())
                .build();
    }

    private AwsCredentialsProvider credentialsProvider() {
        String accessKeyId = awsCredentials.accessKeyId();
        String secretKey = awsCredentials.secretKey();

        if (accessKeyId != null && !accessKeyId.isBlank() &&
                secretKey != null && !secretKey.isBlank()) {
            return StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                            accessKeyId,
                            secretKey
                    )
            );
        }
        // Fallback: use environment or IAM role
        return DefaultCredentialsProvider.create();
    }
}
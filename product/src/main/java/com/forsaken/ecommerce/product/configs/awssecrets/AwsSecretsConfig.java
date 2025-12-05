package com.forsaken.ecommerce.product.configs.awssecrets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forsaken.ecommerce.product.configs.aurora.AwsDbCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class AwsSecretsConfig {

    private final SecretsManagerProperties secretsManagerProperties;

    @Bean
    public AwsAccessCredentials awsAccessCredentials() {
        final String secretName = secretsManagerProperties.accessSecretName();
        final Region region = Region.AP_SOUTH_1;

        final SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build();

        final GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        final GetSecretValueResponse getSecretValueResponse =
                client.getSecretValue(getSecretValueRequest);

        try {
            final ObjectMapper mapper = new ObjectMapper();
            final Map<String, String> secrets =
                    mapper.readValue(getSecretValueResponse.secretString(), Map.class);

            final AwsAccessCredentials awsAccessCredentials = AwsAccessCredentials.builder()
                    .accessKeyId(secrets.get("awsAccessKey"))
                    .secretKey(secrets.get("awsSecretKey"))
                    .region(secrets.get("region"))
                    .build();
            return awsAccessCredentials;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load AWS credentials from Secrets Manager", e);
        }
    }


    @Bean
    public AwsDbCredentials awsDbCredentials() {
        final String secretName = secretsManagerProperties.dbSecretName();
        final Region region = Region.AP_SOUTH_1;

        final SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build();

        final GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        final GetSecretValueResponse getSecretValueResponse =
                client.getSecretValue(getSecretValueRequest);

        try {
            final ObjectMapper mapper = new ObjectMapper();
            final Map<String, String> secrets =
                    mapper.readValue(getSecretValueResponse.secretString(), Map.class);

            final AwsDbCredentials awsDbCredentials = AwsDbCredentials.builder()
                    .userName(secrets.get("postgress_username"))
                    .password(secrets.get("postgress_password"))
                    .host(secrets.get("postgress_host"))
                    .port(secrets.get("port"))
                    .dbName(secrets.get("dbname"))
                    .build();

            return awsDbCredentials;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load AWS credentials from Secrets Manager", e);
        }
    }
}

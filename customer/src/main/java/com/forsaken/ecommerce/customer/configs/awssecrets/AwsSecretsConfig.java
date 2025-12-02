package com.forsaken.ecommerce.customer.configs.awssecrets;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    public AwsCredentials awsCredentials() {
        String secretName = secretsManagerProperties.secretName();
        Region region = Region.AP_SOUTH_1;

        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse getSecretValueResponse =
                client.getSecretValue(getSecretValueRequest);

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> secrets =
                    mapper.readValue(getSecretValueResponse.secretString(), Map.class);

            AwsCredentials props =
                    AwsCredentials.builder()
                            .accessKeyId(secrets.get("awsAccessKey"))
                            .secretKey(secrets.get("awsSecretKey"))
                            .region(secrets.get("region"))
                            .build();
            return props;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load AWS credentials from Secrets Manager", e);
        }
    }
}

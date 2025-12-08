package com.forsaken.ecommerce.payment.configs.kafka;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;


@Validated
@ConfigurationProperties(prefix = "spring.kafka.producer")
public record KafkaProperties(

        @NotBlank
        String topicName,

        @Min(1)
        @Max(1000)
        int partitions,

        @Min(3)
        @Max(31)
        int replicationFactor,

        @Min(1)
        @Max(31)
        int minInSyncReplicas,

        @Min(3)
        @Max(100)
        int retries,

        @Min(100)
        @Max(1500)
        int retryBackOffMs,

        @Pattern(regexp = "^(1|0|-1|all)$",message = "valid values are 0, 1, -1 or all")
        String ack,

        @Pattern(regexp = "^(gzip|snappy|lz4|zstd|none)$",message = "valid values are gzip, snappy, lz4, zstd or give none for default")
        String compressionType,

        @NotBlank
        String schemaRegistryUrl,

        @NotNull
        Boolean isUncleanElection,

        @NotEmpty
        List<String> bootstrapServers,

        @NotNull
        Class<?> keySerializer,

        @NotNull
        Class<?> valueSerializer
) {
}
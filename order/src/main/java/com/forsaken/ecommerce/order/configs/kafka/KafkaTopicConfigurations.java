package com.forsaken.ecommerce.order.configs.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.AbstractMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class KafkaTopicConfigurations {

    private final KafkaProperties kafkaProperties;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        final Map<String, Object> configsMap = Map.ofEntries(
                new AbstractMap.SimpleEntry<>
                        (
                                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
                                kafkaProperties.bootstrapServers()
                        ),
                new AbstractMap.SimpleEntry<>
                        (
                                AdminClientConfig.RETRY_BACKOFF_MS_CONFIG,
                                kafkaProperties.retryBackOffMs()
                        )
        );
        return new KafkaAdmin(configsMap);
    }

    @Bean
    public NewTopic orderTopic() {
        log.error(" Replication Factor: {}", kafkaProperties.replicationFactor());
        return TopicBuilder
                .name(kafkaProperties.topicName())
                .partitions(kafkaProperties.partitions())
                .replicas(kafkaProperties.replicationFactor())
                .configs(constructTopicConfigsMap())
                .build();
    }

    private Map<String, String> constructTopicConfigsMap() {
        return Map.ofEntries(
                new AbstractMap.SimpleEntry<>
                        (
                                TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG,
                                String.valueOf(kafkaProperties.minInSyncReplicas())
                        ),
                new AbstractMap.SimpleEntry<>
                        (
                                TopicConfig.UNCLEAN_LEADER_ELECTION_ENABLE_CONFIG,
                                String.valueOf(kafkaProperties.isUncleanElection())
                        )
        );
    }
}

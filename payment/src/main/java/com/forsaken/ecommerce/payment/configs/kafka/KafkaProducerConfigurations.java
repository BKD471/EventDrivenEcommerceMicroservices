package com.forsaken.ecommerce.payment.configs.kafka;

import com.forsaken.ecommerce.avro.PaymentConfirmation;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.AbstractMap;
import java.util.Map;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfigurations {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, PaymentConfirmation> producerFactory() {
        final Map<String, Object> producerConfigPropsMap = Map.ofEntries(
                new AbstractMap.SimpleEntry<>
                        (
                                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                                kafkaProperties.bootstrapServers()
                        ),
                new AbstractMap.SimpleEntry<>
                        (
                                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                                kafkaProperties.keySerializer().getName()
                        ),
                new AbstractMap.SimpleEntry<>
                        (
                                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                                kafkaProperties.valueSerializer().getName()
                        ),
                new AbstractMap.SimpleEntry<>(
                        SCHEMA_REGISTRY_URL_CONFIG,
                        kafkaProperties.schemaRegistryUrl()
                ),
                new AbstractMap.SimpleEntry<>(
                        "specific.avro.reader",
                        true
                ),
                new AbstractMap.SimpleEntry<>
                        (
                                ProducerConfig.ACKS_CONFIG,
                                kafkaProperties.ack()
                        ),
                new AbstractMap.SimpleEntry<>
                        (
                                ProducerConfig.RETRIES_CONFIG,
                                kafkaProperties.retries()
                        )
        );

        return new DefaultKafkaProducerFactory<>(producerConfigPropsMap);
    }

    @Bean
    public KafkaTemplate<String, PaymentConfirmation> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
package com.forsaken.ecommerce.notification.configs.kafka;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.RecordInterceptor;

@Configuration
public class KafkaTracingConfigurations {

    @Bean
    public RecordInterceptor<String, Object> kafkaTracingInterceptor(
            final ObservationRegistry registry,
            final Tracer tracer
    ) {
        return (record, consumer) -> {
            final Observation observation = Observation.start("kafka.consume", registry);
            observation.lowCardinalityKeyValue("topic", record.topic());
            observation.lowCardinalityKeyValue("partition", String.valueOf(record.partition()));
            try (final Observation.Scope scope = observation.openScope()) {
                return record;
            } finally {
                observation.stop();
            }
        };
    }
}
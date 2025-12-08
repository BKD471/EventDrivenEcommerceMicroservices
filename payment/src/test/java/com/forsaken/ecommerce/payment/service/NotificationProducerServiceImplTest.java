package com.forsaken.ecommerce.payment.service;



import com.forsaken.ecommerce.avro.PaymentConfirmation;
import com.forsaken.ecommerce.avro.PaymentMethod;
import com.forsaken.ecommerce.payment.configs.kafka.KafkaProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;

import java.nio.ByteBuffer;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationProducerServiceImplTest {

    @Mock
    private KafkaProperties kafkaProperties;

    @Mock
    private KafkaTemplate<String, PaymentConfirmation> kafkaTemplate;

    private NotificationProducerServiceImpl service;

    @BeforeEach
    void setup() {
        service = new NotificationProducerServiceImpl(kafkaProperties, kafkaTemplate);
    }

    @Test
    void testSendNotification() {
        // Given
        final PaymentConfirmation confirmation = constructPaymentConfirmation();
        final String topic = "payment-topic";
        when(kafkaProperties.topicName()).thenReturn(topic);
        ArgumentCaptor<Message<PaymentConfirmation>> messageCaptor =
                ArgumentCaptor.forClass(Message.class);

        // When
        service.sendNotification(confirmation);

        // Then
        verify(kafkaTemplate).send(messageCaptor.capture());
        Message<PaymentConfirmation> msg = messageCaptor.getValue();
        assertEquals(confirmation, msg.getPayload());
        assertEquals(topic, msg.getHeaders().get("kafka_topic"));
    }


    private PaymentConfirmation constructPaymentConfirmation() {
        final ByteBuffer amount = ByteBuffer.wrap("150.50".getBytes());
        return PaymentConfirmation.newBuilder()
                .setOrderReference("order-123")
                .setAmount(amount)
                .setCustomerFirstname("John")
                .setCustomerLastname("Doe")
                .setPaymentMethod(PaymentMethod.BITCOIN)
                .setPaymentDate(Instant.now())
                .setCustomerEmail("john@doe.com")
                .build();
    }
}

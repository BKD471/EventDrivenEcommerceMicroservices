package com.forsaken.ecommerce.notification.service;

import com.forsaken.ecommerce.avro.OrderConfirmation;
import com.forsaken.ecommerce.avro.PaymentConfirmation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumerImpl implements INotificationConsumer {

    @KafkaListener(
            topics = "${spring.kafka.consumer.paymentTopicName}",
            groupId = "${spring.kafka.consumer.paymentGroupId}",
            containerFactory = "paymentKafkaListenerContainerFactory"
    )
    @Override
    public void consumePaymentSuccessNotifications(ConsumerRecord<String, PaymentConfirmation> record) throws MessagingException {
        // TODO: Implement the logic to process payment confirmation notifications

        // TODO save to DB or send email/SMS notification
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.orderTopicName}",
            groupId = "${spring.kafka.consumer.orderGroupId}",
            containerFactory = "orderKafkaListenerContainerFactory"
    )
    @Override
    public void consumeOrderConfirmationNotifications(ConsumerRecord<String, OrderConfirmation> record) throws MessagingException {
        // TODO: Implement the logic to process order confirmation notifications

        // TODO save to DB or send email/SMS notification
    }
}

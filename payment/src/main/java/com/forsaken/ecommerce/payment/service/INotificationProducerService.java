package com.forsaken.ecommerce.payment.service;

import com.forsaken.ecommerce.avro.PaymentConfirmation;
import org.springframework.messaging.MessagingException;

public interface INotificationProducerService {

    /**
     * Sends a payment confirmation notification by publishing the provided request
     * to a configured Kafka topic.
     *
     * <p>This method is responsible for serializing and dispatching the given
     * {@link PaymentConfirmation} message to the messaging infrastructure. It does
     * not return a result, but failures during publishing may trigger exceptions
     * depending on the Kafka producer configuration (e.g., synchronous vs asynchronous
     * send, retries, or error handlers).</p>
     *
     * @param request the payment confirmation payload to be published; must not be null
     * @throws IllegalArgumentException if the request is null
     * @throws MessagingException       if the message cannot be published to Kafka
     */
    void sendNotification(final PaymentConfirmation request);
}

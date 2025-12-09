package com.forsaken.ecommerce.payment.service;

import com.forsaken.ecommerce.avro.PaymentConfirmation;
import org.springframework.messaging.MessagingException;

/**
 * Service interface responsible for publishing payment-related notifications
 * to the messaging infrastructure (e.g., Kafka).
 *
 * <p>Implementations of this interface handle the serialization and dispatching
 * of {@link PaymentConfirmation} events to the appropriate topic. This is typically
 * used to notify downstream services—such as order management, inventory, or email
 * services—about successful payment processing.</p>
 *
 * <p>Depending on the underlying Kafka producer configuration, failures during
 * publishing may result in runtime exceptions. Implementations may choose to
 * publish messages synchronously or asynchronously.</p>
 *
 * @see PaymentConfirmation
 */
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

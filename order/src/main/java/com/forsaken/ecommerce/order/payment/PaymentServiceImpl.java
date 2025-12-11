package com.forsaken.ecommerce.order.payment;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements IPaymentService {

    private final IPaymentClient paymentClient;

    @Override
    public CompletableFuture<Integer> pay(final PaymentRequest request) {
        log.info("Payment request: {}", request);
        return CompletableFuture.completedFuture(
                paymentClient.requestOrderPayment(request)
        );
    }
}

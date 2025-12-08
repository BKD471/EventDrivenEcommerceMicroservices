package com.forsaken.ecommerce.payment.service;


import com.forsaken.ecommerce.avro.PaymentConfirmation;
import com.forsaken.ecommerce.avro.PaymentMethod;
import com.forsaken.ecommerce.payment.dto.PaymentRequest;
import com.forsaken.ecommerce.payment.dto.PaymentSummaryDto;
import com.forsaken.ecommerce.payment.model.Payment;
import com.forsaken.ecommerce.payment.repository.IPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Conversions;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements IPaymentService {

    private final IPaymentRepository repository;
    private final INotificationProducerService notificationProducer;

    @Override
    public Integer createPayment(final PaymentRequest request) {
        final Payment payment = this.repository.save(request.toPayment());
        final LocalDateTime localDateTime = LocalDateTime.now();
        final Instant instant = localDateTime.atZone(ZoneId.of("UTC")).toInstant();

        this.notificationProducer.sendNotification(
                new PaymentConfirmation(
                        request.orderReference(),
                        convertBigDecimalToBytes(request.amount()),
                        PaymentMethod.valueOf(request.paymentMethod().name()),
                        request.customer().firstname(),
                        request.customer().lastname(),
                        request.customer().email(),
                        instant,
                        "traceId TODO" // TODO to be done later
                )
        );
        log.info("Created Payment Request: {}", request);
        return payment.getId();
    }

    @Override
    public List<PaymentSummaryDto> getPaymentSummary(
            final LocalDateTime fromDate,
            final LocalDateTime toDate
    ) {
        log.info("Get Payment Summary By Date: {}", fromDate);
        return repository.findPaymentSummaryBetween(fromDate, toDate).stream()
                .map(p -> new PaymentSummaryDto(
                        p.getPaymentMethod(),
                        p.getCount(),
                        p.getTotalAmount()
                ))
                .toList();
    }

    @Override
    public List<Payment> getAllPayments(
            final LocalDateTime fromDate,
            final LocalDateTime toDate
    ) {
        log.info("Get All Payments By Date: {}", fromDate);
        return repository.findAllByCreatedDateBetween(fromDate, toDate);
    }

    private ByteBuffer convertBigDecimalToBytes(final BigDecimal value) {
        if (value == null) return null;

        final Schema DECIMAL_SCHEMA =
                LogicalTypes.decimal(18, 2)
                        .addToSchema(Schema.create(Schema.Type.BYTES));
        final Conversions.DecimalConversion DECIMAL_CONVERSION =
                new Conversions.DecimalConversion();

        return DECIMAL_CONVERSION.toBytes(value, DECIMAL_SCHEMA, DECIMAL_SCHEMA.getLogicalType());
    }
}

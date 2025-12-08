package com.forsaken.ecommerce.payment.service;

import com.forsaken.ecommerce.payment.dto.PaymentRequest;
import com.forsaken.ecommerce.payment.dto.PaymentSummaryDto;
import com.forsaken.ecommerce.payment.model.Payment;

import java.time.LocalDateTime;
import java.util.List;

public interface IPaymentService {


    /**
     * Creates a new payment based on the provided request data.
     *
     * <p>This method validates and processes the incoming {@link PaymentRequest},
     * persists the payment information, and returns the generated payment ID.
     * The implementation is responsible for performing any required business
     * logic such as validation, fee calculation, or status initialization.</p>
     *
     * @param request the payment creation request containing all required payment fields;
     *                must not be null
     * @return the generated unique payment ID
     * @throws IllegalArgumentException if the request is null or contains invalid data
     */
    Integer createPayment(final PaymentRequest request);

    /**
     * Retrieves summarized payment information within an optional date range.
     *
     * <p>This method aggregates payment data (such as total amount, count, or status-based
     * summaries) and returns a list of {@link PaymentSummaryDto} objects. If both
     * <code>fromDate</code> and <code>toDate</code> are provided, only payments created
     * within that range are included. If either parameter is null, the corresponding
     * date boundary is not applied.</p>
     *
     * @param fromDate the optional start date-time for filtering payments; may be null
     * @param toDate   the optional end date-time for filtering payments; may be null
     * @return a list of payment summary DTOs representing aggregated payment results
     */
    List<PaymentSummaryDto> getPaymentSummary(final LocalDateTime fromDate, final LocalDateTime toDate);


    /**
     * Retrieves all payment records within an optional date-time range.
     *
     * <p>If both <code>fromDate</code> and <code>toDate</code> are provided, the method
     * returns only those payments whose creation timestamps fall within the specified
     * interval. If either parameter is null, the respective boundary is not applied.
     * When both parameters are null, all available payments are returned.</p>
     *
     * @param fromDate the optional start date-time for filtering payments; may be null
     * @param toDate   the optional end date-time for filtering payments; may be null
     * @return a list of {@link Payment} entities that match the given date criteria
     */
    List<Payment> getAllPayments(final LocalDateTime fromDate, final LocalDateTime toDate);
}

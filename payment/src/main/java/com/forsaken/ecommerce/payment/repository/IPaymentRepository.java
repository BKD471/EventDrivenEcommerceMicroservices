package com.forsaken.ecommerce.payment.repository;

import com.forsaken.ecommerce.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IPaymentRepository extends JpaRepository<Payment, Integer> {

    @Query("""
                SELECT p.paymentMethod AS paymentMethod,
                       COUNT(p) AS count,
                       SUM(p.amount) AS totalAmount
                FROM Payment p
                WHERE p.createdDate BETWEEN :fromDate AND :toDate
                GROUP BY p.paymentMethod
                ORDER BY p.paymentMethod
            """)
    List<PaymentSummary> findPaymentSummaryBetween(
            @Param("fromDate") final LocalDateTime fromDate,
            @Param("toDate") final LocalDateTime toDate
    );

    List<Payment> findAllByCreatedDateBetween(
            final LocalDateTime fromDate,
            final LocalDateTime toDate
    );
}

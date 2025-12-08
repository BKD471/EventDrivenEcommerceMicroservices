package com.forsaken.ecommerce.payment.repository;

import com.forsaken.ecommerce.payment.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IPaymentRepository extends JpaRepository<Payment, Integer> {

    @Query(value = """
    SELECT p.paymentMethod AS paymentMethod,
           COUNT(p)        AS count,
           SUM(CAST(p.amount AS DECIMAL))   AS totalAmount
    FROM Payment p
    WHERE (:fromDate IS NULL OR p.createdDate >= :fromDate)
      AND (:toDate   IS NULL OR p.createdDate <= :toDate)
    GROUP BY p.paymentMethod
    ORDER BY p.paymentMethod
    """,
            countQuery = """
    SELECT COUNT(DISTINCT p.paymentMethod)
    FROM Payment p
    WHERE (:fromDate IS NULL OR p.createdDate >= :fromDate)
      AND (:toDate   IS NULL OR p.createdDate <= :toDate)
    """)
    Page<PaymentSummary> findPaymentSummaryBetween(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable
    );

    Page<Payment> findAllByCreatedDateBetween(
            LocalDateTime fromDate,
            LocalDateTime toDate,
            Pageable pageable
    );
}

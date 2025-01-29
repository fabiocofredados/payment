package com.ezycollect.payment.repository;

import com.ezycollect.payment.domain.entity.PaymentWebhook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentWebhookRepository extends JpaRepository<PaymentWebhook, Long> {

}

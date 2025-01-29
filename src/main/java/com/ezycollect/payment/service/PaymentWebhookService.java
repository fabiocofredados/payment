package com.ezycollect.payment.service;

import com.ezycollect.payment.domain.entity.Payment;
import com.ezycollect.payment.domain.entity.PaymentWebhook;
import com.ezycollect.payment.domain.entity.Webhook;
import com.ezycollect.payment.enums.WebhookExecutionStatus;
import com.ezycollect.payment.repository.PaymentWebhookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentWebhookService {

    @Autowired
    PaymentWebhookRepository paymentWebhookRepository;

    public PaymentWebhook add(PaymentWebhook paymentWebhook){
        return paymentWebhookRepository.save(paymentWebhook);
    }

    public PaymentWebhook add(Webhook webhook, Payment payment, WebhookExecutionStatus status) {
        PaymentWebhook paymentWebhook = new PaymentWebhook();
        paymentWebhook.setWebhook(webhook);
        paymentWebhook.setPayment(payment);
        paymentWebhook.setStatus(status);
        return paymentWebhookRepository.save(paymentWebhook);
    }

    public List<PaymentWebhook> findAll(){
        List<PaymentWebhook> failedWebhooks = paymentWebhookRepository.findAll();
        return failedWebhooks;
    }

}

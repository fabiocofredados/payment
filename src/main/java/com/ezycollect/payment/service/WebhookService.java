package com.ezycollect.payment.service;

import com.ezycollect.payment.domain.entity.Payment;
import com.ezycollect.payment.domain.entity.Webhook;
import com.ezycollect.payment.enums.WebhookExecutionStatus;
import com.ezycollect.payment.repository.WebhookRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

@Service
public class WebhookService {

    @Autowired
    private final WebhookRepository webhookRepository;
    @Autowired
    private WebhookExecutionService webhookExecutionService;

    @Autowired
    PaymentWebhookService paymentWebhookService;

    public WebhookService(WebhookRepository webhookRepository, WebhookExecutionService webhookExecutionService) {
        this.webhookRepository = webhookRepository;
        this.webhookExecutionService = webhookExecutionService;
    }

    public Webhook add(Webhook webhook){
        webhook.getParams().forEach((w) -> w.setWebhook(webhook));
        webhookRepository.save(webhook);
        return webhook;
    }

    public List<Webhook> findAll(){
        return webhookRepository.findAll();
    }

    @Retryable(
            value = { ResourceAccessException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 800, multiplier = 2)
    )
    public void triggerWebhook(Webhook webhook, Payment payment) {
        String payload = getPayload(payment);
        webhookExecutionService.executeWebhook(webhook, payload);
    }

    @Recover
    public void fallbackForTriggerWebhook(ResourceAccessException ex, Webhook webhook, Payment payment){
        System.out.println("Webhooks execution failed! Fallback method called.");
        paymentWebhookService.add(webhook, payment,WebhookExecutionStatus.FAIL);
        System.out.println("Register saved to be processed later.");

    }

    private String getPayload(Payment payment) {
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = null;
        try {
            payload = objectMapper.writeValueAsString(payment);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return payload;
    }

}

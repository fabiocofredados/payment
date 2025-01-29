package com.ezycollect.payment.service;

import com.ezycollect.payment.domain.entity.Payment;
import com.ezycollect.payment.domain.entity.Webhook;
import com.ezycollect.payment.repository.PaymentRepository;
import com.ezycollect.payment.util.RsaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private WebhookService webhookService;
    @Value("${crypt.public}")
    private String base64PublicKey;
    @Value("${crypt.private}")
    private String base64PrivateKey;
    public List<Payment> findAll(){
        List<Payment> paymentList = paymentRepository.findAll();

        paymentList.forEach((p) -> {
            try {
                p.setCardNumber(RsaService.decryptCardNumber(p.getCardNumber(),base64PrivateKey));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return paymentList;
    }

    public void add(Payment payment) {
        try {
            payment.setCardNumber(RsaService.encryptCardNumber(payment.getCardNumber(),base64PublicKey));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        paymentRepository.save(payment);
        executeWebhooks(payment);
    }

    private void executeWebhooks(Payment payment) {
        List<Webhook> webhooks = webhookService.findAll();

        for (Webhook webhook : webhooks) {
            try{
                webhookService.triggerWebhook(webhook, payment);
            }catch(Exception exception){
                System.out.println("Error executing webhook: " + exception);
            }
        }
    }
}

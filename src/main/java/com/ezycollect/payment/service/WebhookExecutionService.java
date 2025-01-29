package com.ezycollect.payment.service;

import com.ezycollect.payment.domain.entity.Webhook;
import com.ezycollect.payment.domain.entity.WebhookHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class WebhookExecutionService {

    @Autowired
    private RestTemplate restTemplate;

    public void executeWebhook(Webhook webhook, String payload) {
        HttpHeaders headers = new HttpHeaders();
        for(WebhookHeader header : webhook.getParams()){
            headers.add(header.getName(), header.getValue());
        }
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(payload, headers);

        restTemplate.postForObject(webhook.getUrl(), request, String.class);
    }
}
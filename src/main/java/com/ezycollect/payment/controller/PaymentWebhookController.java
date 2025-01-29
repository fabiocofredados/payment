package com.ezycollect.payment.controller;

import com.ezycollect.payment.domain.dto.PaymentWebhookDTO;
import com.ezycollect.payment.domain.dto.WebhookDTO;
import com.ezycollect.payment.domain.entity.PaymentWebhook;
import com.ezycollect.payment.domain.entity.Webhook;
import com.ezycollect.payment.service.PaymentWebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/webhooks/reprocess")
public class PaymentWebhookController {

    @Autowired
    PaymentWebhookService paymentWebhookService;

    ModelMapper modelMapper;

    public PaymentWebhookController(){
        modelMapper = new ModelMapper();
    }

    @Operation(summary = "Get the failed webhooks", description = "Returns a list of webhooks that failed after a payment trigger")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved webhooks must be reprocessed"),
            @ApiResponse(responseCode = "404", description = "There are no failed webhooks")
    })
    @GetMapping
    public ResponseEntity<List<PaymentWebhookDTO>> getAll(){
        List<PaymentWebhook> paymentWebhookList = paymentWebhookService.findAll();
        if (paymentWebhookList.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<PaymentWebhookDTO> dtos = paymentWebhookList
                .stream()
                .map(webhook -> modelMapper.map(webhook, PaymentWebhookDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}

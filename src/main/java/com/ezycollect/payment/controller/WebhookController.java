package com.ezycollect.payment.controller;

import com.ezycollect.payment.domain.dto.PaymentDTO;
import com.ezycollect.payment.domain.dto.WebhookDTO;
import com.ezycollect.payment.domain.entity.Payment;
import com.ezycollect.payment.domain.entity.Webhook;
import com.ezycollect.payment.service.WebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/webhooks")
public class WebhookController {

    @Autowired
    private WebhookService webhookService;

    private ModelMapper modelMapper;
    public WebhookController(){
        modelMapper = new ModelMapper();
    }

    @Operation(summary = "Register webhook", description = "Api to register a webhook to be triggered for each payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered the webhook"),
            @ApiResponse(responseCode = "400", description = "Invalid url supplied"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    public ResponseEntity<WebhookDTO> registerWebhook(@RequestBody WebhookDTO webhook){
        if (webhook.getUrl() == null || webhook.getUrl().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        try{
            webhookService.add(modelMapper.map(webhook, Webhook.class));
        }catch(Exception exception){
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(webhook);
    }

    @Operation(summary = "Get all webhooks", description = "Returns a list of webhooks from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved webhooks list"),
            @ApiResponse(responseCode = "404", description = "Webhooks not found")
    })
    @GetMapping
    public ResponseEntity<List<WebhookDTO>> getAll(){
        List<Webhook> webhookList = webhookService.findAll();
        if (webhookList.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<WebhookDTO> dtos = webhookList
                .stream()
                .map(webhook -> modelMapper.map(webhook, WebhookDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}

package com.ezycollect.payment.controller;

import com.ezycollect.payment.domain.dto.PaymentDTO;
import com.ezycollect.payment.domain.entity.Payment;
import com.ezycollect.payment.service.PaymentService;
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
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    private ModelMapper modelMapper;

    public PaymentController(){
        modelMapper = new ModelMapper();
    }

    @Operation(summary = "Save or update a Payment", description = "Receives a payment and save it on database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully saved the payment"),
            @ApiResponse(responseCode = "400", description = "Invalid card number supplied"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error when saving the payment")
    })
    @PostMapping
    public ResponseEntity<PaymentDTO> save(@RequestBody PaymentDTO payment){
        if (payment.getCardNumber() == null || payment.getCardNumber().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        try{
            paymentService.add(modelMapper.map(payment, Payment.class));
        }catch(Exception exception){
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(payment);
    }

    @Operation(summary = "Get all Payments", description = "Returns a list of payments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved payments list"),
            @ApiResponse(responseCode = "404", description = "Payments not found")
    })
    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAll(){
        List<Payment> paymentList = paymentService.findAll();
        if (paymentList.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<PaymentDTO> dtos = paymentList
                .stream()
                .map(payment -> modelMapper.map(payment, PaymentDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

}

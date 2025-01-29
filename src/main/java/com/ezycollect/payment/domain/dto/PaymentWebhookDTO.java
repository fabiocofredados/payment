package com.ezycollect.payment.domain.dto;

import com.ezycollect.payment.enums.WebhookExecutionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentWebhookDTO {
    PaymentDTO payment;
    WebhookDTO webhook;
    WebhookExecutionStatus status;
}

package com.ezycollect.payment.domain.dto;

import com.ezycollect.payment.domain.entity.WebhookHeader;
import com.ezycollect.payment.enums.WebhookType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebhookDTO {
    private String url;
    private WebhookType type;
    private List<WebhookHeader> params;
}

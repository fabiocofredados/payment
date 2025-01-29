package com.ezycollect.payment.domain.entity;

import com.ezycollect.payment.enums.WebhookType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "webhooks")
public class Webhook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private WebhookType type;

    @OneToMany(
            mappedBy = "webhook",
            cascade = CascadeType.ALL
    )
    private List<WebhookHeader> params;
}

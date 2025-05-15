package com.rca.myspringsecurity.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchased_tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchasedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meter_number_id", nullable = false)
    private MeterNumber meterNumber;

    @Column(nullable = false, length = 16)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenStatus tokenStatus;

    @Column(nullable = false)
    private Integer tokenValueDays;

    @Column(nullable = false)
    private LocalDateTime purchasedDate;

    @Column(nullable = false)
    private BigDecimal amount;
}

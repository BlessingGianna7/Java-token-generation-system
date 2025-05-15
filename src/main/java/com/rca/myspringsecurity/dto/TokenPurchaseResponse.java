package com.rca.myspringsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenPurchaseResponse {
    private String meterNumber;
    private String formattedToken;
    private Integer days;
    private BigDecimal amount;
    private LocalDateTime purchasedDate;
    private LocalDateTime expiryDate;
}

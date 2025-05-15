package com.rca.myspringsecurity.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenPurchaseRequest {
    @NotBlank(message = "Meter number is required")
    @Size(min = 6, max = 6, message = "Meter number must be 6 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]{6}$", message = "Meter number must be 6 alphanumeric characters")
    private String meterNumber;

    @Min(value = 100, message = "Amount must be at least 100 RWF")
    private BigDecimal amount;
}

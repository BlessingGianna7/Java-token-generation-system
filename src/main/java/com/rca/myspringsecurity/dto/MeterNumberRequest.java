package com.rca.myspringsecurity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeterNumberRequest {
    @NotBlank(message = "Meter number is required")
    @Size(min = 6, max = 6, message = "Meter number must be 6 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]{6}$", message = "Meter number must be 6 alphanumeric characters")
    private String meterNumber;

    @NotBlank(message = "National ID is required")
    @Size(min = 16, max = 16, message = "National ID must be 16 characters")
    private String nationalId;
}

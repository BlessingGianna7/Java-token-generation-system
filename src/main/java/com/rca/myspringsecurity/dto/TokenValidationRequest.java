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
public class TokenValidationRequest {
    @NotBlank(message = "Token is required")
    @Size(min = 16, max = 19, message = "Token must be 16 characters (excluding dashes)")
    @Pattern(regexp = "^[0-9-]{16,19}$", message = "Token must contain only digits and optional dashes")
    private String token;
}

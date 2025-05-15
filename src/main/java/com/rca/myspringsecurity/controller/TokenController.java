package com.rca.myspringsecurity.controller;

import com.rca.myspringsecurity.dto.TokenPurchaseRequest;
import com.rca.myspringsecurity.dto.TokenPurchaseResponse;
import com.rca.myspringsecurity.dto.TokenValidationRequest;
import com.rca.myspringsecurity.dto.TokenValidationResponse;
import com.rca.myspringsecurity.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tokens")
@Tag(name = "Token Management", description = "Token Management API")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TokenController {
    @Autowired
    private TokenService tokenService;

    @PostMapping("/purchase")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Purchase a new token")
    public ResponseEntity<TokenPurchaseResponse> purchaseToken(@Valid @RequestBody TokenPurchaseRequest request) {
        TokenPurchaseResponse response = tokenService.purchaseToken(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/validate")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @Operation(summary = "Validate a token")
    public ResponseEntity<TokenValidationResponse> validateToken(@Valid @RequestBody TokenValidationRequest request) {
        TokenValidationResponse response = tokenService.validateToken(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/meter/{meterNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @Operation(summary = "Get all tokens by meter number")
    public ResponseEntity<List<TokenValidationResponse>> getTokensByMeterNumber(@PathVariable String meterNumber) {
        List<TokenValidationResponse> tokens = tokenService.getTokensByMeterNumber(meterNumber);
        return ResponseEntity.ok(tokens);
    }
}

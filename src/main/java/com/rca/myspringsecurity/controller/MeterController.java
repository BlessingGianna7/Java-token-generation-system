package com.rca.myspringsecurity.controller;

import com.rca.myspringsecurity.dto.MeterNumberRequest;
import com.rca.myspringsecurity.dto.MeterNumberResponse;
import com.rca.myspringsecurity.service.MeterService;
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
@RequestMapping("/api/meters")
@Tag(name = "Meter Management", description = "Meter Management API")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MeterController {
    @Autowired
    private MeterService meterService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Register a new meter number")
    public ResponseEntity<MeterNumberResponse> registerMeterNumber(@Valid @RequestBody MeterNumberRequest request) {
        MeterNumberResponse response = meterService.registerMeterNumber(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/owner/{nationalId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all meter numbers by owner's national ID")
    public ResponseEntity<List<MeterNumberResponse>> getMeterNumbersByOwner(@PathVariable String nationalId) {
        List<MeterNumberResponse> meterNumbers = meterService.getMeterNumbersByOwner(nationalId);
        return ResponseEntity.ok(meterNumbers);
    }

    @GetMapping("/{meterNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @Operation(summary = "Get meter number details by meter number")
    public ResponseEntity<MeterNumberResponse> getMeterNumberByNumber(@PathVariable String meterNumber) {
        MeterNumberResponse response = meterService.getMeterNumberByNumber(meterNumber);
        return ResponseEntity.ok(response);
    }
}

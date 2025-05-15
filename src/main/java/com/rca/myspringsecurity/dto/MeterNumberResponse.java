package com.rca.myspringsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeterNumberResponse {
    private Long id;
    private String meterNumber;
    private String ownerNames;
    private String ownerEmail;
    private String ownerPhone;
}

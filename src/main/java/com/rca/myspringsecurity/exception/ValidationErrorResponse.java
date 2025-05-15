package com.rca.myspringsecurity.exception;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private String details;
    private int status;
    private Map<String, String> errors;
}

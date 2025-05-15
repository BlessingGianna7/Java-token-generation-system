package com.rca.myspringsecurity.service;


import com.rca.myspringsecurity.dto.TokenPurchaseRequest;
import com.rca.myspringsecurity.dto.TokenPurchaseResponse;
import com.rca.myspringsecurity.dto.TokenValidationRequest;
import com.rca.myspringsecurity.dto.TokenValidationResponse;
import com.rca.myspringsecurity.exception.BadRequestException;
import com.rca.myspringsecurity.exception.ResourceNotFoundException;
import com.rca.myspringsecurity.model.MeterNumber;
import com.rca.myspringsecurity.model.PurchasedToken;
import com.rca.myspringsecurity.model.TokenStatus;
import com.rca.myspringsecurity.repository.MeterNumberRepository;
import com.rca.myspringsecurity.repository.PurchasedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class TokenService {
    @Autowired
    private PurchasedTokenRepository tokenRepository;

    @Autowired
    private MeterNumberRepository meterNumberRepository;

    private static final int MAX_DAYS = 5 * 365; // 5 years
    private static final BigDecimal PRICE_PER_DAY = new BigDecimal("100");

    @Transactional
    public TokenPurchaseResponse purchaseToken(TokenPurchaseRequest request) {
        // Validate amount
        if (request.getAmount().compareTo(PRICE_PER_DAY) < 0) {
            throw new BadRequestException("Amount must be at least 100 RWF");
        }

        // Calculate days
        int days = request.getAmount().divide(PRICE_PER_DAY, 0, RoundingMode.DOWN).intValue();

        if (days > MAX_DAYS) {
            throw new BadRequestException("Cannot purchase token for more than 5 years");
        }

        // Find meter number
        MeterNumber meterNumber = meterNumberRepository.findByMeterNumber(request.getMeterNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Meter number not found: " + request.getMeterNumber()));

        // Generate token
        String token = generateToken();

        // Save token
        LocalDateTime purchasedDate = LocalDateTime.now();
        PurchasedToken purchasedToken = PurchasedToken.builder()
                .meterNumber(meterNumber)
                .token(token)
                .tokenStatus(TokenStatus.NEW)
                .tokenValueDays(days)
                .purchasedDate(purchasedDate)
                .amount(request.getAmount())
                .build();

        tokenRepository.save(purchasedToken);

        // Format token for response
        String formattedToken = formatToken(token);

        return TokenPurchaseResponse.builder()
                .meterNumber(meterNumber.getMeterNumber())
                .formattedToken(formattedToken)
                .days(days)
                .amount(request.getAmount())
                .purchasedDate(purchasedDate)
                .expiryDate(purchasedDate.plusDays(days))
                .build();
    }

    public TokenValidationResponse validateToken(TokenValidationRequest request) {
        // Clean token (remove dashes)
        String cleanToken = request.getToken().replaceAll("-", "");

        // Find token
        PurchasedToken token = tokenRepository.findByToken(cleanToken)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found: " + request.getToken()));

        return TokenValidationResponse.builder()
                .meterNumber(token.getMeterNumber().getMeterNumber())
                .token(formatToken(token.getToken()))
                .status(token.getTokenStatus().name())
                .days(token.getTokenValueDays())
                .amount(token.getAmount())
                .purchasedDate(token.getPurchasedDate())
                .expiryDate(token.getPurchasedDate().plusDays(token.getTokenValueDays()))
                .build();
    }

    public List<TokenValidationResponse> getTokensByMeterNumber(String meterNumber) {
        MeterNumber meter = meterNumberRepository.findByMeterNumber(meterNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Meter number not found: " + meterNumber));

        List<PurchasedToken> tokens = tokenRepository.findByMeterNumber(meter);

        return tokens.stream()
                .map(token -> TokenValidationResponse.builder()
                        .meterNumber(token.getMeterNumber().getMeterNumber())
                        .token(formatToken(token.getToken()))
                        .status(token.getTokenStatus().name())
                        .days(token.getTokenValueDays())
                        .amount(token.getAmount())
                        .purchasedDate(token.getPurchasedDate())
                        .expiryDate(token.getPurchasedDate().plusDays(token.getTokenValueDays()))
                        .build())
                .collect(Collectors.toList());
    }

    private String generateToken() {
        Random random = new Random();
        StringBuilder token = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            token.append(random.nextInt(10));
        }

        return token.toString();
    }

    private String formatToken(String token) {
        // Format: XXXX-XXXX-XXXX-XXXX
        return token.replaceAll("(.{4})", "$1-").substring(0, 19);
    }
}

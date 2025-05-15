package com.rca.myspringsecurity.repository;


import com.rca.myspringsecurity.model.MeterNumber;
import com.rca.myspringsecurity.model.PurchasedToken;
import com.rca.myspringsecurity.model.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchasedTokenRepository extends JpaRepository<PurchasedToken, Long> {
    List<PurchasedToken> findByMeterNumber(MeterNumber meterNumber);
    Optional<PurchasedToken> findByToken(String token);

    @Query("SELECT p FROM PurchasedToken p WHERE p.tokenStatus = :status AND p.purchasedDate <= :expiryTime")
    List<PurchasedToken> findTokensAboutToExpire(TokenStatus status, LocalDateTime expiryTime);
}

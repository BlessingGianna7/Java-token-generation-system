package com.rca.myspringsecurity.service;


import com.rca.myspringsecurity.model.MeterNumber;
import com.rca.myspringsecurity.model.Notification;
import com.rca.myspringsecurity.model.PurchasedToken;
import com.rca.myspringsecurity.model.TokenStatus;
import com.rca.myspringsecurity.repository.NotificationRepository;
import com.rca.myspringsecurity.repository.PurchasedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private PurchasedTokenRepository tokenRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Scheduled(fixedRate = 3600000) // Run every hour
    @Transactional
    public void checkExpiringTokens() {
        LocalDateTime expiryTime = LocalDateTime.now().plusHours(5);
        List<PurchasedToken> expiringTokens = tokenRepository.findTokensAboutToExpire(TokenStatus.NEW, expiryTime);

        for (PurchasedToken token : expiringTokens) {
            MeterNumber meterNumber = token.getMeterNumber();
            String message = generateExpiryMessage(meterNumber, token);

            Notification notification = Notification.builder()
                    .meterNumber(meterNumber)
                    .message(message)
                    .issuedDate(LocalDateTime.now())
                    .sent(false)
                    .build();

            notificationRepository.save(notification);
        }
    }

    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    @Transactional
    public void sendPendingNotifications() {
        List<Notification> pendingNotifications = notificationRepository.findBySentFalse();

        for (Notification notification : pendingNotifications) {
            boolean sent = sendEmailNotification(notification);

            if (sent) {
                notification.setSent(true);
                notificationRepository.save(notification);
            }
        }
    }

    private String generateExpiryMessage(MeterNumber meterNumber, PurchasedToken token) {
        return String.format(
                "Dear %s, REG is pleased to remind you that the token in the %s is going to expire in 5 hours. Please purchase a new token.",
                meterNumber.getOwner().getNames(),
                meterNumber.getMeterNumber()
        );
    }

    private boolean sendEmailNotification(Notification notification) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(notification.getMeterNumber().getOwner().getEmail());
            message.setSubject("Token Expiry Notification");
            message.setText(notification.getMessage());

            emailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}


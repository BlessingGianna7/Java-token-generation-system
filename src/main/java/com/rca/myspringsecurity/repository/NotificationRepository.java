package com.rca.myspringsecurity.repository;


import com.rca.myspringsecurity.model.MeterNumber;
import com.rca.myspringsecurity.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByMeterNumber(MeterNumber meterNumber);
    List<Notification> findBySentFalse();
}



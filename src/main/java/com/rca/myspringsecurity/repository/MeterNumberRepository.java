package com.rca.myspringsecurity.repository;


import com.rca.myspringsecurity.model.MeterNumber;
import com.rca.myspringsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeterNumberRepository extends JpaRepository<MeterNumber, Long> {
    Optional<MeterNumber> findByMeterNumber(String meterNumber);
    List<MeterNumber> findByOwner(User owner);
    boolean existsByMeterNumber(String meterNumber);
}


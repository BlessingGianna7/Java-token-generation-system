package com.rca.myspringsecurity.service;

import com.rca.myspringsecurity.dto.MeterNumberRequest;
import com.rca.myspringsecurity.dto.MeterNumberResponse;
import com.rca.myspringsecurity.exception.ResourceAlreadyExistsException;
import com.rca.myspringsecurity.exception.ResourceNotFoundException;
import com.rca.myspringsecurity.model.MeterNumber;
import com.rca.myspringsecurity.model.User;
import com.rca.myspringsecurity.repository.MeterNumberRepository;
import com.rca.myspringsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeterService {
    @Autowired
    private MeterNumberRepository meterNumberRepository;

    @Autowired
    private UserRepository userRepository;

    public MeterNumberResponse registerMeterNumber(MeterNumberRequest request) {
        if (meterNumberRepository.existsByMeterNumber(request.getMeterNumber())) {
            throw new ResourceAlreadyExistsException("Meter number already exists");
        }

        User owner = userRepository.findByNationalId(request.getNationalId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with national ID: " + request.getNationalId()));

        MeterNumber meterNumber = MeterNumber.builder()
                .meterNumber(request.getMeterNumber())
                .owner(owner)
                .build();

        MeterNumber savedMeterNumber = meterNumberRepository.save(meterNumber);

        return mapToResponse(savedMeterNumber);
    }

    public List<MeterNumberResponse> getMeterNumbersByOwner(String nationalId) {
        User owner = userRepository.findByNationalId(nationalId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with national ID: " + nationalId));

        List<MeterNumber> meterNumbers = meterNumberRepository.findByOwner(owner);

        return meterNumbers.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public MeterNumberResponse getMeterNumberByNumber(String meterNumber) {
        MeterNumber meter = meterNumberRepository.findByMeterNumber(meterNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Meter number not found: " + meterNumber));

        return mapToResponse(meter);
    }

    private MeterNumberResponse mapToResponse(MeterNumber meterNumber) {
        return MeterNumberResponse.builder()
                .id(meterNumber.getId())
                .meterNumber(meterNumber.getMeterNumber())
                .ownerNames(meterNumber.getOwner().getNames())
                .ownerEmail(meterNumber.getOwner().getEmail())
                .ownerPhone(meterNumber.getOwner().getPhone())
                .build();
    }
}

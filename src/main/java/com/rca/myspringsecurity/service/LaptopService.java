package com.rca.myspringsecurity.service;

import com.rca.myspringsecurity.entity.Laptop;
import com.rca.myspringsecurity.entity.Student;
import com.rca.myspringsecurity.error.CustomException;
import com.rca.myspringsecurity.repository.LaptopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LaptopService {

    private final StudentService studentService;

    @Autowired
    private LaptopRepository laptopRepository;

    public LaptopService(StudentService studentService, LaptopRepository laptopRepository) {
        this.studentService = studentService;
        this.laptopRepository = laptopRepository;
    }

    public Laptop addLaptop(Laptop lap){
        String sn= lap.getSn();
        if (sn == null || sn.length() < 8) {
            throw new CustomException("Serial number must be at least 8 characters long");
        }
         return laptopRepository.save(lap);

    }


}


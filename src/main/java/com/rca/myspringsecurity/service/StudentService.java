package com.rca.myspringsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rca.myspringsecurity.entity.Student;
import com.rca.myspringsecurity.repository.StudentRepository;
@Service
public class StudentService {
    @Autowired
    private StudentRepository repo;
    public void addStudent(Student st) {
        repo.save(st);
    }
}


package com.rca.myspringsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rca.myspringsecurity.entity.Student;
import com.rca.myspringsecurity.repository.StudentRepository;

import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository repo;
    @Autowired
    public StudentService(StudentRepository repo) {
        this.repo = repo;
    }

    public void addStudent(Student st) {
        repo.save(st);
    }
    public Optional<Student> getStudentById(Integer Id){
        return repo.findById(Id);
    }


}


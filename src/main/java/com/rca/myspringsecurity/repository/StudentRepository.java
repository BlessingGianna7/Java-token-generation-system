package com.rca.myspringsecurity.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.rca.myspringsecurity.entity.Student;
@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
}

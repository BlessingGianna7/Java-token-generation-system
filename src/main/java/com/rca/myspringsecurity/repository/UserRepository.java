package com.rca.myspringsecurity.repository;

import com.rca.myspringsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNationalId(String nationalId);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByNationalId(String nationalId);
}

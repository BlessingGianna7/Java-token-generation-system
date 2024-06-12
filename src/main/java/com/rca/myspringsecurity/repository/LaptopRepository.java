package com.rca.myspringsecurity.repository;

import com.rca.myspringsecurity.entity.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaptopRepository extends JpaRepository<Laptop, Integer> {
}

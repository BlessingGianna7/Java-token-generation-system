package com.rca.myspringsecurity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
@Entity
@Data
@AllArgsConstructor
public class Laptop {
    @Id
    @GeneratedValue
    private int lapId;
    @Column(nullable = false, length = 100)
    private String brand;
    @Column(nullable = false, length = 100)
    private String sn;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    public Laptop() {

    }
    public Laptop(String brand,String sn, Student student) {
        super();
        this.brand = brand;
        this.sn = sn;
        this.student = student;
    }
}


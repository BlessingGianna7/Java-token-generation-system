package com.rca.myspringsecurity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meter_numbers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeterNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Meter number is required")
    @Size(min = 6, max = 6, message = "Meter number must be 6 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]{6}$", message = "Meter number must be 6 alphanumeric characters")
    @Column(unique = true)
    private String meterNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "meterNumber", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PurchasedToken> tokens = new ArrayList<>();
}

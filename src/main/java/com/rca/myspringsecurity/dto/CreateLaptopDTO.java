package com.rca.myspringsecurity.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateLaptopDTO {
//    @NotEmpty(message = "Brand is required!")
    String brand;

//    @Size(min = 1, max = 100, message = "Serial Number must be between 1 and 100 characters!")
//    @NotEmpty(message = "Serial Number is required!")
    String sn;
//    @NotNull(message = "Student Id is required!")
    Integer studentId;

}

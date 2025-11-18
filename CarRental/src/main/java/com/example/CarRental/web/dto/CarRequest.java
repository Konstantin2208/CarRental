package com.example.CarRental.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarRequest {
    @NotBlank
    private UUID id;
    @NotBlank
    private String brand;
    @NotBlank
    private String model;
    @NotBlank
    private int year;
    @NotBlank
    private double pricePerDay;
    @NotBlank
    private boolean available;
    @URL
    @NotBlank
    private String image;
}

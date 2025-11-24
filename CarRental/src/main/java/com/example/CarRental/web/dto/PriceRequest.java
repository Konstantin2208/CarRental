package com.example.CarRental.web.dto;

import lombok.*;

import java.time.LocalDate;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PriceRequest {
    private double pricePerDay;
    private LocalDate startDate;
    private LocalDate endDate;


}
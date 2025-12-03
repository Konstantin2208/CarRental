package com.pricing.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PriceRequest {
    private double pricePerDay;
    private LocalDate startDate;
    private LocalDate endDate;

}

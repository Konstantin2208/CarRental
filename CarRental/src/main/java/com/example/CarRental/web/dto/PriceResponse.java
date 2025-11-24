package com.example.CarRental.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceResponse {
    private long days;
    private double pricePerDay;
    private double subtotal;
    private double serviceFee;
    private double total;
}

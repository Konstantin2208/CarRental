package com.pricing.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceResponse {
    private long days;
    private double pricePerDay;
    private double subtotal;
    private double serviceFee;
    private double total;
}

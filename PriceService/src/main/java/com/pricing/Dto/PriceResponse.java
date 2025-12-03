package com.pricing.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class PriceResponse {
    private long days;
    private double pricePerDay;
    private double subtotal;
    private double serviceFee;
    private double total;
}

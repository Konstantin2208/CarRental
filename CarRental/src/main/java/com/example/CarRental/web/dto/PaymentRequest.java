package com.example.CarRental.web.dto;

import com.example.CarRental.model.Rental;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {

    private UUID rentalId;
    private String method;
}

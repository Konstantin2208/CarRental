package com.example.CarRental.web.dto;

import com.example.CarRental.model.PaymentMethod;
import com.example.CarRental.model.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
public class PaymentResponse {
    private UUID id;
    private UUID rentalId;
    private double amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private LocalDateTime createdAt;
}

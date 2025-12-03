package com.example.CarRental.web.dto;

import com.example.CarRental.model.PaymentMethod;
import com.example.CarRental.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@AllArgsConstructor
@Getter
@Setter
public class PaymentResponse {
    private UUID id;
    private UUID rentalId;
    private double amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private LocalDateTime createdAt;
}

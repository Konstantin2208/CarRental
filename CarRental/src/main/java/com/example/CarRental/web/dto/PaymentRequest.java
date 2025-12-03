package com.example.CarRental.web.dto;

import com.example.CarRental.model.Rental;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentRequest {

    private UUID rentalId;
    private String method;
}

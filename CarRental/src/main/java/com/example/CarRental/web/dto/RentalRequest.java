package com.example.CarRental.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RentalRequest {
    @NotBlank
    private UUID carId;
    @NotBlank
    private LocalDate startDate;
    @NotBlank
    private LocalDate endDate;

}

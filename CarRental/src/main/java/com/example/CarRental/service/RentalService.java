package com.example.CarRental.service;

import com.example.CarRental.exception.NotFoundException;
import com.example.CarRental.model.Rental;
import com.example.CarRental.model.RentalStatus;
import com.example.CarRental.repository.CarRepo;
import com.example.CarRental.repository.RentalRepo;
import com.example.CarRental.web.dto.RentalRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RentalService {
    private final RentalRepo rentalRepo;
    private final CarRepo carRepo;


    @Autowired
    public RentalService(RentalRepo rentalRepo, CarRepo carRepo) {
        this.rentalRepo = rentalRepo;
        this.carRepo = carRepo;
    }

    public UUID createRental(RentalRequest rentalRequest, UUID userId) {
        Rental rental = Rental.builder()
                .car(carRepo.findById(rentalRequest.getCarId())
                        .orElseThrow(() -> new NotFoundException("Car not found")))
                .userId(userId)
                .startDate(rentalRequest.getStartDate())
                .endDate(rentalRequest.getEndDate())
                .status(RentalStatus.PENDING)
                .build();

        rentalRepo.save(rental);

        return rental.getId();
    }


    public Rental getRentalById(UUID rentalId) {
       return rentalRepo.findById(rentalId).orElseThrow(() -> new NotFoundException("Rental not found"));
    }
}

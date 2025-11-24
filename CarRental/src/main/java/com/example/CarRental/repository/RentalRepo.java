package com.example.CarRental.repository;

import com.example.CarRental.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface RentalRepo extends JpaRepository<Rental, UUID> {

    List<Rental> findByUserId(UUID userId);
    List<Rental> findByCarId(UUID carId);

    List<Rental> findAllByUserId(UUID userId);


    Optional<Rental> findById(UUID rentalID);

}
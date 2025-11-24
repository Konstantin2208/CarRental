package com.example.CarRental.repository;

import com.example.CarRental.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface PaymentRepo extends JpaRepository<Payment, UUID> {

    Payment findByRentalId(UUID rentalId);

    List<Payment> findAllByUserId(UUID userId);
}

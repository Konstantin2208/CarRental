package com.example.CarRental.repository;

import com.example.CarRental.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface CarRepo extends JpaRepository<Car, UUID> {

    List<Car> findByAvailableTrue();
    List<Car> findByBrand(String brand);

}

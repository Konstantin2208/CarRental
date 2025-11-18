package com.example.CarRental.service;

import com.example.CarRental.model.Car;
import com.example.CarRental.repository.CarRepo;
import com.example.CarRental.web.dto.CarRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarService {
    private final CarRepo carRepo;

    @Autowired
    public CarService(CarRepo carRepo) {
        this.carRepo = carRepo;
    }

    public List<Car> getAllCars() {
        return carRepo.findAll();
    }

    public Optional<Car> getCarById(UUID id) {
        return carRepo.findById(id);
    }

    public void save(CarRequest carRequest) {
        Car car= Car.builder()
                .brand(carRequest.getBrand())
                .model(carRequest.getModel())
                .year(carRequest.getYear())
                .pricePerDay(carRequest.getPricePerDay())
                .available(carRequest.isAvailable())
                .image(carRequest.getImage())
                .build();

        carRepo.save(car);
    }

    public void deleteCar(UUID id) {
       carRepo.deleteById(id);
    }
}

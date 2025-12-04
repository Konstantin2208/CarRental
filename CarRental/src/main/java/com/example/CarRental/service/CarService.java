package com.example.CarRental.service;

import com.example.CarRental.model.Car;
import com.example.CarRental.repository.CarRepo;
import com.example.CarRental.web.dto.CarRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "cars")
    public List<Car> getAllCars() {
        return carRepo.findAll();
    }
    @Cacheable(value = "carDetails", key = "#id")
    public Car getCarById(UUID id) {
        return carRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id: " + id));
    }
    @CacheEvict(value = "cars", allEntries = true)
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
    @CacheEvict(value = "carDetails", key = "#car.id")
    public  void update(CarRequest carRequest, Car car) {
        car.setBrand(carRequest.getBrand());
        car.setModel(carRequest.getModel());
        car.setYear(carRequest.getYear());
        car.setPricePerDay(carRequest.getPricePerDay());
        car.setAvailable(carRequest.isAvailable());
        car.setImage(carRequest.getImage());
        carRepo.save(car);
    }
    public static CarRequest getCarRequest(Car car) {
        CarRequest carRequest = new CarRequest();
        carRequest.setId(car.getId());
        carRequest.setBrand(car.getBrand());
        carRequest.setModel(car.getModel());
        carRequest.setYear(car.getYear());
        carRequest.setPricePerDay(car.getPricePerDay());
        carRequest.setAvailable(car.isAvailable());
        carRequest.setImage(car.getImage());
        return carRequest;
    }

    public void deleteCar(UUID id) {
       carRepo.deleteById(id);
    }
}

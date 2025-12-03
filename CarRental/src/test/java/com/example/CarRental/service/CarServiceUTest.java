package com.example.CarRental.service;

import com.example.CarRental.model.Car;
import com.example.CarRental.repository.CarRepo;
import com.example.CarRental.web.dto.CarRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CarServiceUTest {
    @Mock
    private  CarRepo carRepo;
    @InjectMocks
    private CarService carService;

    @Test
    void shouldReturnAllCars() {
        List<Car> cars = List.of(
                Car.builder().brand("BMW").build(),
                Car.builder().brand("Audi").build()
        );

        when(carRepo.findAll()).thenReturn(cars);

        List<Car> result = carService.getAllCars();

        assertEquals(2, result.size());
        verify(carRepo).findAll();
    }
    @Test
    void shouldReturnCar_WhenIdExists() {
        UUID id=UUID.randomUUID();
        Car car = Car.builder().id(id).brand("BMW").build();

        when(carRepo.findById(id)).thenReturn(Optional.of(car));

        Car result = carService.getCarById(id);

        assertEquals("BMW", result.getBrand());
        verify(carRepo).findById(id);
    }

    @Test
    void shouldThrowError_WhenCarNotFound() {
        UUID id = UUID.randomUUID();

        when(carRepo.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> carService.getCarById(id));

        verify(carRepo).findById(id);
    }

    @Test
    void shouldSaveNewCar() {
        CarRequest carRequest = CarRequest.builder()
                .brand("BMW")
                .model("X5")
                .year(2022)
                .pricePerDay(100)
                .available(true)
                .image("img.png")
                .build();

        carService.save(carRequest);

        verify(carRepo).save(argThat(car ->
                car.getBrand().equals("BMW") &&
                        car.getModel().equals("X5") &&
                        car.getYear() == 2022 &&
                        car.getPricePerDay() == 100 &&
                        car.isAvailable() &&
                        car.getImage().equals("img.png")
        ));
    }
    @Test
    void shouldUpdateCar() {
        Car car = new Car(UUID.randomUUID(), "BMW", "X5", 2020, 100.0,true,"image.png");

        CarRequest carRequest = CarRequest.builder()
                .brand("NewBrand")
                .model("NewModel")
                .year(2024)
                .pricePerDay(150)
                .available(true)
                .image("new.png")
                .build();

        carService.update(carRequest, car);

        assertEquals("NewBrand",car.getBrand());
        assertEquals("NewModel", car.getModel());
        assertEquals(2024, car.getYear());
        assertEquals(150,car.getPricePerDay());
        assertTrue(car.isAvailable());
        assertEquals("new.png",car.getImage());
        verify(carRepo).save(car);
    }
    @Test
    void shouldConvertCarToCarRequest() {
        Car car = Car.builder()
                .id(UUID.randomUUID())
                .brand("BMW")
                .model("X5")
                .year(2022)
                .pricePerDay(100)
                .available(true)
                .image("img.png")
                .build();

        CarRequest carRequest = CarService.getCarRequest(car);

        assertEquals(car.getId(), carRequest.getId());
        assertEquals("BMW", carRequest.getBrand());
        assertEquals("X5", carRequest.getModel());
        assertEquals(2022, carRequest.getYear());
        assertEquals(100, carRequest.getPricePerDay());
        assertTrue(carRequest.isAvailable());
        assertEquals("img.png", carRequest.getImage());
    }
    @Test
    void shouldDeleteCar() {
        UUID id = UUID.randomUUID();

        carService.deleteCar(id);

        verify(carRepo).deleteById(id);
    }




}
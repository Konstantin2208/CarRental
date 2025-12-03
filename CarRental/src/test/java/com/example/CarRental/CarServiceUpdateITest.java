package com.example.CarRental.service;

import com.example.CarRental.model.Car;
import com.example.CarRental.repository.CarRepo;
import com.example.CarRental.web.dto.CarRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CarServiceUpdateITest {

    @Autowired
    private CarService carService;

    @Autowired
    private CarRepo carRepo;

    private Car savedCar;

    @BeforeEach
    void setUp() {

        CarRequest carRequest = new CarRequest();
        carRequest.setBrand("Audi");
        carRequest.setModel("A4");
        carRequest.setYear(2020);
        carRequest.setPricePerDay(100.0);
        carRequest.setAvailable(true);
        carRequest.setImage("audi.jpg");

        carService.save(carRequest);


        savedCar = carRepo.findAll().get(0);
    }

    @Test
    void update_shouldModifyExistingCar() {

        CarRequest updateRequest = new CarRequest();
        updateRequest.setBrand("BMW");
        updateRequest.setModel(savedCar.getModel());
        updateRequest.setYear(savedCar.getYear());
        updateRequest.setPricePerDay(savedCar.getPricePerDay());
        updateRequest.setAvailable(savedCar.isAvailable());
        updateRequest.setImage(savedCar.getImage());


        carService.update(updateRequest, savedCar);


        Car updatedCar = carService.getCarById(savedCar.getId());


        assertNotNull(updatedCar);
        assertEquals("BMW", updatedCar.getBrand()); // check that brand was updated
        assertEquals(savedCar.getModel(), updatedCar.getModel()); // unchanged
        assertEquals(savedCar.getYear(), updatedCar.getYear()); // unchanged
        assertEquals(savedCar.getPricePerDay(), updatedCar.getPricePerDay());
        assertEquals(savedCar.isAvailable(), updatedCar.isAvailable());
        assertEquals(savedCar.getImage(), updatedCar.getImage());
    }
}

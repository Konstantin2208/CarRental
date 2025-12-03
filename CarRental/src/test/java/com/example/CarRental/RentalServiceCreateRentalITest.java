package com.example.CarRental;

import com.example.CarRental.model.Car;
import com.example.CarRental.model.Rental;
import com.example.CarRental.model.RentalStatus;
import com.example.CarRental.repository.CarRepo;
import com.example.CarRental.repository.RentalRepo;
import com.example.CarRental.service.RentalService;
import com.example.CarRental.web.dto.RentalRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RentalServiceCreateRentalITest {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private CarRepo carRepo;

    @Autowired
    private RentalRepo rentalRepo;

    private Car savedCar;
    private UUID userId;

    @BeforeEach
    void setUp() {

        savedCar = Car.builder()
                .brand("Audi")
                .model("A4")
                .year(2020)
                .pricePerDay(100.0)
                .available(true)
                .image("audi.jpg")
                .build();
        carRepo.save(savedCar);


        userId = UUID.randomUUID();
    }

    @Test
    void createRental_shouldStoreRentalAndReturnId() {

        RentalRequest rentalRequest = new RentalRequest();
        rentalRequest.setCarId(savedCar.getId());
        rentalRequest.setStartDate(LocalDate.now());
        rentalRequest.setEndDate(LocalDate.now().plusDays(3));


        UUID rentalId = rentalService.createRental(rentalRequest, userId);


        Rental rental = rentalRepo.findById(rentalId).orElse(null);


        assertNotNull(rental);
        assertEquals(savedCar.getId(), rental.getCar().getId());
        assertEquals(userId, rental.getUserId());
        assertEquals(RentalStatus.PENDING, rental.getStatus());
        assertEquals(rentalRequest.getStartDate(), rental.getStartDate());
        assertEquals(rentalRequest.getEndDate(), rental.getEndDate());
    }
}

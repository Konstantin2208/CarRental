package com.example.CarRental.service;

import com.example.CarRental.exception.NotFoundException;
import com.example.CarRental.model.Car;
import com.example.CarRental.model.Rental;
import com.example.CarRental.model.RentalStatus;
import com.example.CarRental.repository.CarRepo;
import com.example.CarRental.repository.RentalRepo;
import com.example.CarRental.web.dto.RentalRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RentalServiceUTest {

    @Mock
    private RentalRepo rentalRepo;

    @Mock
    private CarRepo carRepo;

    @InjectMocks
    private RentalService rentalService;

    @Test
    void shouldCreateRental_whenCarExists() {
        UUID userId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();
        UUID rentalId = UUID.randomUUID();

        RentalRequest request = RentalRequest.builder()
                .carId(carId)
                .startDate(LocalDate.of(2025, 12, 5))
                .endDate(LocalDate.of(2025, 12, 10))
                .build();

        Car car = Car.builder()
                .id(carId)
                .brand("Toyota")
                .model("Corolla")
                .year(2023)
                .pricePerDay(50)
                .available(true)
                .image("image.png")
                .build();

        when(carRepo.findById(carId)).thenReturn(Optional.of(car));

        when(rentalRepo.save(any(Rental.class))).thenAnswer(invocation -> {
            Rental rental = invocation.getArgument(0);
            rental.setId(rentalId);
            return rental;
        });

        UUID resultId = rentalService.createRental(request, userId);

        assertNotNull(resultId);
        assertEquals(rentalId, resultId);
        verify(rentalRepo).save(any(Rental.class));
    }

    @Test
    void shouldThrowNotFoundException_whenCarDoesNotExist() {
        UUID userId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();

        RentalRequest request = RentalRequest.builder()
                .carId(carId)
                .startDate(LocalDate.of(2025, 12, 5))
                .endDate(LocalDate.of(2025, 12, 10))
                .build();

        when(carRepo.findById(carId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> rentalService.createRental(request, userId));
        verify(rentalRepo, never()).save(any(Rental.class));
    }

    @Test
    void shouldReturnRental_whenRentalExists() {
        UUID rentalId = UUID.randomUUID();
        Rental rental = Rental.builder()
                .id(rentalId)
                .status(RentalStatus.PENDING)
                .build();

        when(rentalRepo.findById(rentalId)).thenReturn(Optional.of(rental));

        Rental result = rentalService.getRentalById(rentalId);

        assertEquals(rental, result);
    }

    @Test
    void shouldThrowNotFoundException_whenRentalDoesNotExist() {
        UUID rentalId = UUID.randomUUID();

        when(rentalRepo.findById(rentalId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> rentalService.getRentalById(rentalId));
    }
}

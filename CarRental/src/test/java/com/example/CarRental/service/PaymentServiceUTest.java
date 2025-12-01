package com.example.CarRental.service;

import com.example.CarRental.feign.PriceClient;
import com.example.CarRental.model.*;
import com.example.CarRental.repository.PaymentRepo;
import com.example.CarRental.web.dto.PaymentRequest;
import com.example.CarRental.web.dto.PriceRequest;
import com.example.CarRental.web.dto.PriceResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceUTest {

    @Mock
    private RentalService rentalService;

    @Mock
    private PriceClient priceClient;

    @Mock
    private PaymentRepo paymentRepo;

    @InjectMocks
    private PaymentService paymentService;


    @Test
    void shouldReturnPriceResponse() {
        UUID rentalId = UUID.randomUUID();

        Car car = Car.builder().pricePerDay(100).build();
        Rental rental = Rental.builder()
                .id(rentalId)
                .car(car)
                .startDate(LocalDate.of(2025, 12, 1))
                .endDate(LocalDate.of(2025, 12, 5))
                .build();

        PriceResponse mockPriceResponse = PriceResponse.builder()
                .days(4)
                .pricePerDay(100)
                .subtotal(400)
                .serviceFee(10)
                .total(410)
                .build();

        when(rentalService.getRentalById(rentalId)).thenReturn(rental);
        when(priceClient.calculatePrice(any(PriceRequest.class))).thenReturn(mockPriceResponse);

        PriceResponse result = paymentService.calculateRentalPriceDetailed(rentalId);

        assertEquals(4, result.getDays());
        assertEquals(100, result.getPricePerDay());
        assertEquals(400, result.getSubtotal());
        assertEquals(10, result.getServiceFee());
        assertEquals(410, result.getTotal());

        verify(rentalService).getRentalById(rentalId);
        verify(priceClient, times(1)).calculatePrice(any(PriceRequest.class));
    }

    @Test
    void shouldCreatePayment() {
        UUID rentalId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        PaymentRequest request = PaymentRequest.builder()
                .rentalId(rentalId)
                .method("CARD")
                .build();

        Car car = Car.builder().pricePerDay(100).build();
        Rental rental = Rental.builder()
                .id(rentalId)
                .car(car)
                .startDate(LocalDate.of(2025, 12, 1))
                .endDate(LocalDate.of(2025, 12, 5))
                .build();

        PriceResponse priceResponse = PriceResponse.builder()
                .days(4)
                .pricePerDay(100)
                .subtotal(400)
                .serviceFee(10)
                .total(410)
                .build();

        Payment savedPayment = Payment.builder()
                .rentalId(rentalId)
                .userId(userId)
                .amount(410)
                .method(PaymentMethod.CARD)
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        when(rentalService.getRentalById(rentalId)).thenReturn(rental);
        when(priceClient.calculatePrice(any(PriceRequest.class))).thenReturn(priceResponse);
        when(paymentRepo.save(any(Payment.class))).thenReturn(savedPayment);

        Payment result = paymentService.createPayment(request, userId);

        assertEquals(410, result.getAmount());
        assertEquals(userId, result.getUserId());
        assertEquals(rentalId, result.getRentalId());
        assertEquals(PaymentStatus.PENDING, result.getStatus());
        assertEquals(PaymentMethod.CARD, result.getMethod());

        verify(rentalService).getRentalById(rentalId);
        verify(priceClient, times(1)).calculatePrice(any(PriceRequest.class));
        verify(paymentRepo).save(any(Payment.class));
    }
}

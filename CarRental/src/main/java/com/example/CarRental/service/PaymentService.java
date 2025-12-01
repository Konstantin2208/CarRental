package com.example.CarRental.service;

import com.example.CarRental.feign.PriceClient;
import com.example.CarRental.model.Payment;
import com.example.CarRental.model.PaymentMethod;
import com.example.CarRental.model.PaymentStatus;
import com.example.CarRental.model.Rental;
import com.example.CarRental.repository.PaymentRepo;
import com.example.CarRental.web.dto.PaymentRequest;
import com.example.CarRental.web.dto.PriceRequest;
import com.example.CarRental.web.dto.PriceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    private final RentalService rentalService;
    private final PriceClient priceClient;
    private final PaymentRepo paymentRepo;

    @Autowired
    public PaymentService(RentalService rentalService, PriceClient priceClient, PaymentRepo paymentRepo) {
        this.rentalService = rentalService;
        this.priceClient = priceClient;
        this.paymentRepo = paymentRepo;
    }

    public PriceResponse calculateRentalPriceDetailed(UUID rentalId) {
        Rental rental = rentalService.getRentalById(rentalId);

        PriceRequest request = new PriceRequest(
                rental.getCar().getPricePerDay(),
                rental.getStartDate(),
                rental.getEndDate()
        );

        PriceResponse clientResponse = priceClient.calculatePrice(request);

        PriceResponse priceResponse = PriceResponse.builder()
                .days(clientResponse.getDays())
                .pricePerDay(clientResponse.getPricePerDay())
                .subtotal(clientResponse.getSubtotal())
                .serviceFee(clientResponse.getServiceFee())
                .total(clientResponse.getTotal())
                .build();

        return priceResponse;
    }


    public Payment createPayment(PaymentRequest request,UUID userId) {
        PriceResponse price = calculateRentalPriceDetailed(request.getRentalId());

        Payment payment = Payment.builder()
                .rentalId(request.getRentalId())
                .userId(userId)
                .amount(price.getTotal())
                .method(PaymentMethod.valueOf(request.getMethod()))
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return paymentRepo.save(payment);
    }

}

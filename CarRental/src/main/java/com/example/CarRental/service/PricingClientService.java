package com.example.CarRental.service;

import com.example.CarRental.feign.PriceClient;
import com.example.CarRental.model.Car;
import com.example.CarRental.web.dto.PriceRequest;
import com.example.CarRental.web.dto.PriceResponse;
import com.example.CarRental.web.dto.RentalRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PricingClientService {

    private final PriceClient priceClient;

    @Autowired
    public PricingClientService(PriceClient priceClient) {
        this.priceClient = priceClient;
    }

    public PriceResponse calculateRentalPrice(Car car, RentalRequest req) {
       PriceRequest priceReq= PriceRequest.builder()
               .pricePerDay(car.getPricePerDay())
               .startDate(req.getStartDate())
               .endDate(req.getEndDate())
               .build();

        return priceClient.calculatePrice(priceReq);
    }
}


package com.pricing.Service;

import com.pricing.Dto.PriceRequest;
import com.pricing.Dto.PriceResponse;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

@Service
public class PricingService {

    private static final double serviceFee= 10.00;

    public PriceResponse calculate(PriceRequest request) {
        if ((request.getPricePerDay() == 0) || (request.getStartDate() == null) || (request.getEndDate() == null)) {
            throw new IllegalArgumentException("PricePerDay, startDate and endDate are required");
        }
        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        if (days <= 0) {
            throw new IllegalArgumentException("End date must be after Start date");
        }
        double subtotal = request.getPricePerDay() * days;
        double total = subtotal + serviceFee;

        return new PriceResponse(days, request.getPricePerDay(), subtotal, serviceFee, total);
    }
}

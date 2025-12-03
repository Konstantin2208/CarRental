package com.pricing.Service;

import com.pricing.Dto.PriceRequest;
import com.pricing.Dto.PriceResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PricingServiceUTest {

    private final PricingService pricingService = new PricingService();

    @Test
    void shouldCalculatePriceCorrectly() {
        PriceRequest request = new PriceRequest(
                50.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 6)
        );

        PriceResponse response = pricingService.calculate(request);

        assertEquals(5, response.getDays());
        assertEquals(50.0, response.getPricePerDay());
        assertEquals(250.0, response.getSubtotal());
        assertEquals(10.0, response.getServiceFee());
        assertEquals(260.0, response.getTotal());
    }

    @Test
    void shouldThrowException_whenMissingFields() {
        PriceRequest request = new PriceRequest(
                0,
                null,
                null
        );

        assertThrows(IllegalArgumentException.class, () -> pricingService.calculate(request));
    }

    @Test
    void shouldThrowException_whenEndDateBeforeStartDate() {
        PriceRequest request = new PriceRequest(
                40.0,
                LocalDate.of(2025, 1, 10),
                LocalDate.of(2025, 1, 5)
        );

        assertThrows(IllegalArgumentException.class, () -> pricingService.calculate(request));
    }
}

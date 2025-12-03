package com.pricing;

import com.pricing.Dto.PriceRequest;
import com.pricing.Dto.PriceResponse;
import com.pricing.Service.PricingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PricingServiceITest {

    @Autowired
    private PricingService pricingService;

    @Test
    void calculate_shouldReturnCorrectPriceResponse() {

        double pricePerDay = 100.0;
        LocalDate startDate = LocalDate.of(2025, 12, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 5); //
        PriceRequest request = new PriceRequest(pricePerDay, startDate, endDate);


        PriceResponse response = pricingService.calculate(request);


        assertNotNull(response);
        assertEquals(4, response.getDays());
        assertEquals(pricePerDay, response.getPricePerDay());
        assertEquals(400.0, response.getSubtotal());
        assertEquals(400.0 + response.getServiceFee(), response.getTotal());
    }

    @Test
    void calculate_shouldThrowException_whenDatesAreInvalid() {
        PriceRequest request = new PriceRequest(100, LocalDate.of(2025,12,5), LocalDate.of(2025,12,1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> pricingService.calculate(request));

        assertEquals("End date must be after start date", exception.getMessage());
    }

    @Test
    void calculate_shouldThrowException_whenFieldsAreMissing() {
        PriceRequest request = new PriceRequest(0, null, null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> pricingService.calculate(request));

        assertEquals("All fields are required", exception.getMessage());
    }
}

package com.example.CarRental.feign;

import com.example.CarRental.web.dto.PriceRequest;
import com.example.CarRental.web.dto.PriceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", url = "http://localhost:8081/api/v1/pricing")
public interface PriceClient {
    @PostMapping("/calculate")
    PriceResponse calculatePrice(@RequestBody PriceRequest request);
}

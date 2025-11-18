package com.pricing.Controller;

import com.pricing.Dto.PriceRequest;
import com.pricing.Dto.PriceResponse;
import com.pricing.Service.PricingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pricing")
public class PricingController {
    private final PricingService pricingService;

    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<PriceResponse> calculate(@RequestBody PriceRequest request) {
        PriceResponse resp = pricingService.calculate(request);
        return ResponseEntity.ok(resp);
    }
}

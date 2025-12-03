package com.pricing.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pricing.Dto.PriceRequest;
import com.pricing.Dto.PriceResponse;
import com.pricing.Service.PricingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PricingController.class)
class PricingControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PricingService pricingService;

    @Test
    void calculate_shouldReturnPriceResponse() throws Exception {


        PriceResponse mockResponse = new PriceResponse(
                3,
                50.0,
                150.0,
                20.0,
                170.0
        );

        when(pricingService.calculate(any(PriceRequest.class)))
                .thenReturn(mockResponse);


        PriceRequest request = new PriceRequest(
                50.0,
                LocalDate.of(2025, 1, 10),
                LocalDate.of(2025, 1, 13)
        );

        mockMvc.perform(
                        post("/api/v1/pricing/calculate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("days").value(3))
                .andExpect(jsonPath("pricePerDay").value(50.0))
                .andExpect(jsonPath("subtotal").value(150.0))
                .andExpect(jsonPath("serviceFee").value(20.0))
                .andExpect(jsonPath("total").value(170.0));
    }
}

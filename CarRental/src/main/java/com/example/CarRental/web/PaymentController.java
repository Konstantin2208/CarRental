package com.example.CarRental.web;

import com.example.CarRental.model.Payment;
import com.example.CarRental.model.Rental;
import com.example.CarRental.security.UserData;
import com.example.CarRental.service.PaymentService;
import com.example.CarRental.service.RentalService;
import com.example.CarRental.web.dto.PaymentRequest;
import com.example.CarRental.web.dto.PriceResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    private final RentalService rentalService;
    private final PaymentService paymentService;

    public PaymentController(RentalService rentalService, PaymentService paymentService) {
        this.rentalService = rentalService;
        this.paymentService = paymentService;
    }

    @GetMapping("/{rentalId}")
    public ModelAndView getPaymentPage(@PathVariable UUID rentalId) {

        Rental rental = rentalService.getRentalById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Rental not found"));

        PriceResponse priceResponse= paymentService.calculateRentalPriceDetailed(rentalId);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setRentalId(rentalId);

        ModelAndView modelAndView = new ModelAndView("payment");
        modelAndView.addObject("rental", rental);
        modelAndView.addObject("price", priceResponse);
        modelAndView.addObject("paymentRequest", paymentRequest);

        return modelAndView;
    }

    @PostMapping("/{rentalId}")
    public String makePayment(@PathVariable UUID rentalId,
                              @ModelAttribute PaymentRequest paymentRequest,@AuthenticationPrincipal UserData userData) {

        paymentRequest.setRentalId(rentalId);
        Payment payment = paymentService.createPayment(paymentRequest,userData.getUserId());

        return "redirect:/my-rentals";
    }
}

package com.example.CarRental.web;

import com.example.CarRental.model.Car;
import com.example.CarRental.security.UserData;
import com.example.CarRental.service.CarService;
import com.example.CarRental.service.RentalService;
import com.example.CarRental.web.dto.RentalRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.UUID;


@Controller
@RequestMapping("/rental-form")
public class RentalFormController {
    private final CarService carService;
    private final RentalService rentalService;

    @Autowired
    public RentalFormController(CarService carService, RentalService rentalService) {
        this.carService = carService;
        this.rentalService = rentalService;
    }



    @GetMapping("/{carId}")
    public ModelAndView getRentalForm(@PathVariable UUID carId) {


        Car car = carService.getCarById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));

        ModelAndView modelAndView = new ModelAndView("rental-form");
        modelAndView.addObject("rentalRequest", new RentalRequest());
        modelAndView.addObject("car", car);

        return modelAndView;
    }

    @PostMapping("/{carId}")
    public String createRental(@PathVariable UUID carId,
                               @ModelAttribute RentalRequest rentalRequest,
                               @AuthenticationPrincipal UserData userData) {

        rentalRequest.setCarId(carId);
        UUID rentalId = rentalService.createRental(rentalRequest, userData.getUserId());
        return "redirect:/payment/"+ rentalId;
    }

}

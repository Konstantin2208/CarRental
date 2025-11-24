package com.example.CarRental.web;

import com.example.CarRental.model.Car;
import com.example.CarRental.model.Rental;
import com.example.CarRental.repository.CarRepo;
import com.example.CarRental.repository.RentalRepo;
import com.example.CarRental.security.UserData;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/my-rentals")
public class MyRentalsController {

    private final RentalRepo rentalRepo;


    public MyRentalsController(RentalRepo rentalRepo) {
        this.rentalRepo = rentalRepo;

    }
    @GetMapping
    public ModelAndView getMyRentalsPage(@AuthenticationPrincipal UserData userData){
        ModelAndView modelAndView= new ModelAndView("my-rentals");
        List<Rental> rentals = rentalRepo.findAllByUserId(userData.getUserId());

        modelAndView.addObject("rentals" , rentals);

        return modelAndView;
    }
}

package com.example.CarRental.web;

import com.example.CarRental.model.Car;
import com.example.CarRental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/cars")
public class CarController {
    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }


    @GetMapping
    public ModelAndView getCarsPage(){

        List<Car> cars = carService.getAllCars();
            ModelAndView modelAndView = new ModelAndView("cars");
            modelAndView.addObject("cars", cars);
            return modelAndView;


    }
    @GetMapping("/{id}")
    public ModelAndView getCarDetails(@PathVariable UUID id){
        Optional<Car> car= carService.getCarById(id);
        ModelAndView modelAndView= new ModelAndView("car-details");
        modelAndView.addObject("car", car);
        return modelAndView;

    }

}

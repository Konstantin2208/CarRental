package com.example.CarRental.web;

import com.example.CarRental.model.Car;
import com.example.CarRental.repository.CarRepo;
import com.example.CarRental.service.CarService;
import com.example.CarRental.web.dto.CarRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CarService carService;
    private final CarRepo carRepo;

    @Autowired
    public AdminController(CarService carService, CarRepo carRepo) {
        this.carService = carService;
        this.carRepo = carRepo;
    }

    @GetMapping
    public ModelAndView getAdminCarPage() {
        List<Car> cars = carService.getAllCars();
        ModelAndView modelAndView = new ModelAndView("admin-cars");
        modelAndView.addObject("cars", cars);
        modelAndView.addObject("carRequest", new CarRequest());
        return modelAndView;
    }

    @PostMapping("/save")
    public String addCar(@ModelAttribute("carRequest") CarRequest carRequest) {
        carService.save(carRequest);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable UUID id) {
        Car car = carRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id: " + id));

        CarRequest carRequest = new CarRequest();
        carRequest.setId(car.getId());
        carRequest.setBrand(car.getBrand());
        carRequest.setModel(car.getModel());
        carRequest.setYear(car.getYear());
        carRequest.setPricePerDay(car.getPricePerDay());
        carRequest.setAvailable(car.isAvailable());
        carRequest.setImage(car.getImage());

        ModelAndView modelAndView = new ModelAndView("admin-cars");
        modelAndView.addObject("cars", carRepo.findAll());
        modelAndView.addObject("carRequest", carRequest);
        modelAndView.addObject("editMode", true);
        return modelAndView;
    }

    @PostMapping("/update")
    public String updateCar(@ModelAttribute("carRequest") CarRequest carRequest) {
        if (carRequest.getId() == null) {
            return "redirect:/admin";
        }

        Car car = carRepo.findById(carRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));
        car.setBrand(carRequest.getBrand());
        car.setModel(carRequest.getModel());
        car.setYear(carRequest.getYear());
        car.setPricePerDay(carRequest.getPricePerDay());
        car.setAvailable(carRequest.isAvailable());
        car.setImage(carRequest.getImage());
        carRepo.save(car);

        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteCar(@PathVariable UUID id) {
        carService.deleteCar(id);
        return "redirect:/admin";
    }
}

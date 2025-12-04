package com.example.CarRental.web;

import com.example.CarRental.model.User;
import com.example.CarRental.service.UserService;
import com.example.CarRental.web.dto.LoginRequest;
import com.example.CarRental.web.dto.RegisterRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class IndexController {
    private final UserService userService;

    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String index() {
        return "index";
    }
    @GetMapping("/register")
    public ModelAndView getRegisterPage(){
        ModelAndView modelAndView=new ModelAndView("register");
        modelAndView.addObject("registerRequest", new RegisterRequest());

        return modelAndView;
    }
    @PostMapping("/register")
    public String register(@Valid RegisterRequest registerRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "/register";
        }
        userService.register(registerRequest);

        return "redirect:/login";
    }
    @GetMapping("/login")
    public ModelAndView getLoginPage(){
        ModelAndView modelAndView= new ModelAndView("login");
        modelAndView.addObject("loginRequest", new LoginRequest());

        return modelAndView;

    }



}

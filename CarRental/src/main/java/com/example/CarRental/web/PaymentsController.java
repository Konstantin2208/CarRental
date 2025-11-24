package com.example.CarRental.web;

import com.example.CarRental.model.Payment;
import com.example.CarRental.repository.PaymentRepo;
import com.example.CarRental.security.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/payments")
public class PaymentsController {
    private final PaymentRepo paymentRepo;

    @Autowired
    public PaymentsController(PaymentRepo paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    @GetMapping
    public ModelAndView getPaymentsPage(@AuthenticationPrincipal UserData userData){
        ModelAndView modelAndView= new ModelAndView("payments");
        List<Payment> payments = paymentRepo.findAllByUserId(userData.getUserId());

        modelAndView.addObject("payments", payments);
        return modelAndView;
    }


}

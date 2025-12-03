package com.example.CarRental.web;

import com.example.CarRental.model.Car;
import com.example.CarRental.model.Rental;
import com.example.CarRental.model.Role;
import com.example.CarRental.security.UserData;
import com.example.CarRental.repository.RentalRepo;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MyRentalsController.class)
public class MyRentalsControllerApiTest {

    @MockitoBean
    private RentalRepo rentalRepo;

    @Autowired
    private MockMvc mockMvc;



    @Test
    void getMyRentalsPage_shouldReturn200OkAndRentals() throws Exception {
        UUID userId = UUID.randomUUID();
        UserData userData = new UserData(userId, "titi", "123123", Role.USER);


        Car car1 = Car.builder()
                .id(UUID.randomUUID())
                .brand("Toyota")
                .model("Corolla")
                .build();

        Rental rental1 = Rental.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .car(car1)
                .build();

        Car car2 = Car.builder()
                .id(UUID.randomUUID())
                .brand("Honda")
                .model("Civic")
                .build();

        Rental rental2 = Rental.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .car(car2)
                .build();
        List<Rental> rentals = List.of(rental1, rental2);

        when(rentalRepo.findAllByUserId(userId)).thenReturn(rentals);

        MockHttpServletRequestBuilder requestBuilder = get("/my-rentals")
                .with(user(userData));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("my-rentals"))
                .andExpect(model().attribute("rentals", rentals));
    }
}

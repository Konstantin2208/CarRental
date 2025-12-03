package com.example.CarRental.web;

import com.example.CarRental.model.Car;
import com.example.CarRental.service.CarService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CarControllerApiTest {

    @MockitoBean
    private CarService carService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void getCarsPage_shouldReturnCarsAnd200Ok() throws Exception {
        List<Car> cars = List.of(
                Car.builder().brand("BMW").model("X5").build(),
                Car.builder().brand("Audi").model("A6").build()
        );

        when(carService.getAllCars()).thenReturn(cars);

        MockHttpServletRequestBuilder requestBuilder = get("/cars");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("cars"))
                .andExpect(model().attribute("cars", cars));
    }

    @Test
    void getCarDetails_shouldReturnCarAnd200Ok() throws Exception {
        UUID id = UUID.randomUUID();
        Car car = Car.builder().id(id).brand("BMW").model("X5").build();

        when(carService.getCarById(id)).thenReturn(car);

        MockHttpServletRequestBuilder requestBuilder = get("/cars/{id}", id);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("car-details"))
                .andExpect(model().attribute("car", car));
    }
}

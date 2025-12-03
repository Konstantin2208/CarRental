package com.example.CarRental.web;

import com.example.CarRental.model.Car;
import com.example.CarRental.repository.CarRepo;
import com.example.CarRental.service.CarService;
import com.example.CarRental.web.dto.CarRequest;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdminControllerApiTest {

    @MockitoBean
    private CarService carService;

    @MockitoBean
    private CarRepo carRepo;

    @Autowired
    private MockMvc mockMvc;

    @Captor
    private ArgumentCaptor<CarRequest> carRequestArgumentCaptor;

    private Car car;
    private UUID carId;

    @BeforeEach
    void setup() {
        carId = UUID.randomUUID();
        car = Car.builder()
                .id(carId)
                .brand("BMW")
                .model("M3")
                .year(2023)
                .pricePerDay(150.0)
                .available(true)
                .image("image.png")
                .build();
    }

    @Test
    void getAdminCarPage_shouldReturnCarsAnd200Ok() throws Exception {
        List<Car> cars = List.of(car);
        when(carService.getAllCars()).thenReturn(cars);

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-cars"))
                .andExpect(model().attributeExists("cars"))
                .andExpect(model().attributeExists("carRequest"));

        verify(carService).getAllCars();
    }

    @Test
    void addCar_shouldRedirectAndInvokeServiceSave() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post("/admin/save")
                .param("brand", "BMW")
                .param("model", "M3")
                .param("year", "2023")
                .param("pricePerDay", "150.0")
                .param("available", "true")
                .param("image", "image.png")
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(carService).save(carRequestArgumentCaptor.capture());
        CarRequest dto = carRequestArgumentCaptor.getValue();
        assertEquals("BMW", dto.getBrand());
        assertEquals("M3", dto.getModel());
        assertEquals(2023, dto.getYear());
        assertEquals(150.0, dto.getPricePerDay());
        assertEquals(true, dto.isAvailable());
        assertEquals("image.png", dto.getImage());
    }

    @Test
    void showUpdateForm_shouldReturn200AndModelAttributes() throws Exception {
        when(carService.getCarById(carId)).thenReturn(car);
        when(carRepo.findAll()).thenReturn(List.of(car));

        mockMvc.perform(get("/admin/update/" + carId))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-cars"))
                .andExpect(model().attributeExists("cars"))
                .andExpect(model().attributeExists("carRequest"))
                .andExpect(model().attributeExists("editMode"));

        verify(carService).getCarById(carId);
    }

    @Test
    void updateCar_shouldCallServiceAndRedirect() throws Exception {
        when(carService.getCarById(carId)).thenReturn(car);

        MockHttpServletRequestBuilder requestBuilder = post("/admin/update")
                .param("id", carId.toString())
                .param("brand", "BMW")
                .param("model", "M5")
                .param("year", "2021")
                .param("pricePerDay", "200.0")
                .param("available", "true")
                .param("image", "newimage.png")
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(carService).update(any(CarRequest.class), eq(car));
    }

    @Test
    void deleteCar_shouldCallServiceAndRedirect() throws Exception {
        mockMvc.perform(get("/admin/delete/" + carId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(carService).deleteCar(carId);
    }
}

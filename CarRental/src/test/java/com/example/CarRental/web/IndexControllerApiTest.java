package com.example.CarRental.web;

import com.example.CarRental.service.UserService;
import com.example.CarRental.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(IndexController.class)
public class IndexControllerApiTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;
    @Captor
    private ArgumentCaptor<RegisterRequest>registerRequestArgumentCaptor;

    @Test
    void getIndexEndpoint_shouldReturn200OkAndIndexView() throws Exception {
        MockHttpServletRequestBuilder requestBuilder= get("/");

        mockMvc.perform(requestBuilder)
                .andExpect(view().name("index"))
                .andExpect(status().isOk());


    }
    @Test
    void getRegisterPage_shouldReturn200OkAndRegisterView() throws Exception {
        MockHttpServletRequestBuilder requestBuilder= get("/register");

        mockMvc.perform(requestBuilder)
                .andExpect(view().name("register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("registerRequest"));
    }

    @Test
    void postRegister_shouldReturn200OkAnRedirectToLoginAndInvokeRegisterServiceMethod() throws Exception {
        MockHttpServletRequestBuilder requestBuilder= post("/register")
                .formField("username", "titi2208")
                .formField("email", "titi@gmail.com")
                .formField("password", "123123")
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
        verify(userService).register(registerRequestArgumentCaptor.capture());
        RegisterRequest dto=registerRequestArgumentCaptor.getValue();
        assertEquals("titi2208", dto.getUsername());
        assertEquals("titi@gmail.com", dto.getEmail());
        assertEquals("123123", dto.getPassword());



    }
    @Test
    void postRegister_withValidationErrors_shouldReturnRegisterView() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post("/register")
                .formField("username", "")
                .formField("email", "badEmail")
                .formField("password", "")
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("/register"))
                .andExpect(model().attributeExists("org.springframework.validation.BindingResult.registerRequest"));
    }
    @Test
    void getLoginPage_shouldReturn200OkAndLoginView() throws Exception {
        MockHttpServletRequestBuilder requestBuilder= get("/login");

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginRequest"));
    }

}

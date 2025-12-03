package com.example.CarRental.service;

import com.example.CarRental.exception.NotFoundException;
import com.example.CarRental.model.Role;
import com.example.CarRental.model.User;
import com.example.CarRental.repository.UserRepo;
import com.example.CarRental.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Collections;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;
    @Test
    void shouldCreateAdminProfile_whenUsersIsEmpty(){
        RegisterRequest registerRequest= RegisterRequest.builder()
                .username("Konstantin")
                .password("123123")
                .email("konstantin@gmail.com")
                .build();
        when(userRepo.findAll()).thenReturn(Collections.emptyList());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPass");

        userService.register(registerRequest);

        verify(userRepo).save(argThat(savedUser ->
                savedUser.getUsername().equals("Konstantin") &&
                        savedUser.getPassword().equals("encodedPass") &&
                        savedUser.getEmail().equals("konstantin@gmail.com") &&
                        savedUser.getRole() == Role.ADMIN
        ));
    }
    @Test
    void shouldCreateUserProfile_whenUsersIsNotEmpty(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("Konstantin");
        registerRequest.setPassword("123123");
        registerRequest.setEmail("konstantin@gmail.com");

        when(userRepo.findAll()).thenReturn(Collections.singletonList(new User()));
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPass");

        userService.register(registerRequest);

        verify(userRepo).save(argThat(user ->
                user.getRole() == Role.USER &&
                        user.getUsername().equals("Konstantin") &&
                        user.getPassword().equals("encodedPass")&&
                        user.getEmail().equals("konstantin@gmail.com")

        ));

    }
    @Test
    void loadUserByUsername_shouldThrowException_whenUserDoesNotExist() {
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.loadUserByUsername("UnknownUser"));

        assertEquals("User not found", exception.getMessage());
    }
}

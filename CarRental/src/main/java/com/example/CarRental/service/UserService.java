package com.example.CarRental.service;

import com.example.CarRental.exception.NotFoundException;
import com.example.CarRental.model.Role;
import com.example.CarRental.model.User;
import com.example.CarRental.repository.UserRepo;
import com.example.CarRental.security.UserData;
import com.example.CarRental.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepo userRepo) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    public void register(RegisterRequest registerRequest){
        User user= User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .role(Role.USER)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
        userRepo.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         User user = userRepo.findByUsername(username).orElseThrow(()-> new NotFoundException("User not found"));


        return new UserData(user.getId(),username,user.getPassword(),user.getRole());
    }
}

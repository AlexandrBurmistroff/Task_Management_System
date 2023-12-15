package com.burmistrov.task.management.system.controller;

import com.burmistrov.task.management.system.dto.jwt.JwtRequest;
import com.burmistrov.task.management.system.dto.jwt.JwtResponse;
import com.burmistrov.task.management.system.dto.user.IncomeUserDto;
import com.burmistrov.task.management.system.dto.user.UserDto;
import com.burmistrov.task.management.system.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/register")
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDto> createNewUser(@RequestBody IncomeUserDto incomeUserDto) {
        log.info("Create new user");
        return authService.createNewUser(incomeUserDto);
    }
}

package com.burmistrov.task.management.system.service;

import com.burmistrov.task.management.system.dto.jwt.JwtRequest;
import com.burmistrov.task.management.system.dto.user.IncomeUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {

    ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest);

    ResponseEntity<?> createNewUser(@RequestBody IncomeUserDto incomeUserDto);
}

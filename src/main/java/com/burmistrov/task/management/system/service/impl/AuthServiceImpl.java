package com.burmistrov.task.management.system.service.impl;

import com.burmistrov.task.management.system.dto.jwt.JwtRequest;
import com.burmistrov.task.management.system.dto.jwt.JwtResponse;
import com.burmistrov.task.management.system.dto.user.IncomeUserDto;
import com.burmistrov.task.management.system.dto.user.UserDto;
import com.burmistrov.task.management.system.entity.User;
import com.burmistrov.task.management.system.exception.UnauthorizedException;
import com.burmistrov.task.management.system.service.AuthService;
import com.burmistrov.task.management.system.service.UserService;
import com.burmistrov.task.management.system.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;


    @Override
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        User user = userService.findByUserByEmail(authRequest.getEmail());
        String password = user.getPassword();
        if (!(passwordEncoder.matches(authRequest.getPassword(), password))) {
            throw new UnauthorizedException("Incorrect login or password");
        }
        UserDetails userDetails = userService.loadUserByEmail(authRequest.getEmail());
        String token = jwtTokenUtils.creationToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @Override
    public ResponseEntity<?> createNewUser(IncomeUserDto incomeUserDto) {
        UserDto userDto = userService.addUser(incomeUserDto);
        return ResponseEntity.ok(userDto);
    }
}

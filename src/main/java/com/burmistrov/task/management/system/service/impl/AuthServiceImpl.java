package com.burmistrov.task.management.system.service.impl;

import com.burmistrov.task.management.system.config.CustomUserDetailsService;
import com.burmistrov.task.management.system.dto.jwt.JwtRequest;
import com.burmistrov.task.management.system.dto.jwt.JwtResponse;
import com.burmistrov.task.management.system.dto.user.NewUserDto;
import com.burmistrov.task.management.system.dto.user.UserDto;
import com.burmistrov.task.management.system.entity.User;
import com.burmistrov.task.management.system.exception.BadRequestException;
import com.burmistrov.task.management.system.exception.UnauthorizedException;
import com.burmistrov.task.management.system.service.AuthService;
import com.burmistrov.task.management.system.service.UserService;
import com.burmistrov.task.management.system.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public ResponseEntity<JwtResponse> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            User user = customUserDetailsService.findUserByEmail(authRequest.getEmail())
                    .orElseThrow(() -> new UnauthorizedException("Incorrect login or password"));

            if ((passwordEncoder.matches(authRequest.getPassword(), user.getPassword()))
                    && user.getEmail() != null) {

                UserDetails userDetails = customUserDetailsService.loadUserByEmail(authRequest.getEmail());
                String token = jwtTokenUtils.creationToken(userDetails);
                return ResponseEntity.ok(new JwtResponse(token));
            }
        } catch (UnauthorizedException e) {
            log.error("Incorrect login or password, UnauthorizedException");
        }
        log.error("Incorrect login or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Override
    public ResponseEntity<UserDto> createNewUser(NewUserDto newUserDto) {
        try {
            if ((newUserDto.getEmail() != null)
                    && (newUserDto.getPassword() != null)
                    && (newUserDto.getUsername() != null)) {

                UserDto userDto = userService.addUser(newUserDto);
                return ResponseEntity.ok(userDto);
            }
        } catch (BadRequestException e) {
            log.error("Bad request exception");
        }
        log.error("Bad request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}

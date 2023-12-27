package com.burmistrov.task.management.system.controller;

import com.burmistrov.task.management.system.dto.jwt.JwtRequest;
import com.burmistrov.task.management.system.dto.jwt.JwtResponse;
import com.burmistrov.task.management.system.dto.user.NewUserDto;
import com.burmistrov.task.management.system.dto.user.UserDto;
import com.burmistrov.task.management.system.service.impl.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for registration and login by email and password. Issuing a token
 */
@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {

    private final AuthServiceImpl authService;

    @Operation(summary = "CREATE AUTHENTICATION TOKEN", description = "Endpoint to create an authentication token")
    @ApiResponse(responseCode = "200", description = "Authentication token created successfully",
            content = @Content(schema = @Schema(implementation = JwtResponse.class)))
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> createAuthToken(@RequestBody @Valid JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @Operation(summary = "CREATE NEW USER", description = "Endpoint to create a new user")
    @ApiResponse(responseCode = "201", description = "User created successfully",
            content = @Content(schema = @Schema(implementation = UserDto.class)))
    @PostMapping("/register")
    public ResponseEntity<UserDto> createNewUser(@RequestBody @Valid NewUserDto newUserDto) {
        return authService.createNewUser(newUserDto);
    }
}

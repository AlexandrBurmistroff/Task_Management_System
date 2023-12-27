package com.burmistrov.task.management.system.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class NewUserDto {

    @NotBlank(message = "The name cannot be empty. Enter your name")
    @Size(min = 1, message = "The name cannot be shorter than 1 letter")
    private String username;

    @NotBlank(message = "The email cannot be empty. Enter your email")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "The password cannot be empty. Enter your password")
    @Size(min = 6, message = "The password cannot be shorter than 6 characters")
    private String password;
}

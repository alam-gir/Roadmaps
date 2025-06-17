package com.roadmaps.Roadmaps.modules.authentication.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotEmpty(message = "Email is required!")
    @Email(message = "Enter a valid email.")
    String email;

    @NotEmpty(message = "Password is required!")
    String password;
}

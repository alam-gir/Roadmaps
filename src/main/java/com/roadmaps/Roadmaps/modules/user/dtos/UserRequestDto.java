package com.roadmaps.Roadmaps.modules.user.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequestDto {
    @NotBlank(message = "Name is required!")
    @Size(max = 50, min = 3)
    String name;

    @NotBlank(message = "Email is required!")
    @Email(message = "Enter a valid email.")
    String email;

    @NotBlank(message = "Password is required!")
    @Size(min = 6, max = 16, message = "Password length must be between 6 ot 16!")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).*$",
            message = "Password must contain at least one letter and one number!"
    )
    String password;
}

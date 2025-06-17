package com.roadmaps.Roadmaps.modules.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDto {
    @NotEmpty(message = "Name is required!")
    @Size(max = 50, min = 3)
    String name;

    @NotEmpty(message = "Email is required!")
    @Email(message = "Enter a valid email.")
    String email;

    @NotEmpty(message = "Password is required!")
    @Size(min = 6, max = 16, message = "Password length must be between 6 ot 16!")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).*$",
            message = "Password must contain at least one letter and one number!"
    )
    String password;
}

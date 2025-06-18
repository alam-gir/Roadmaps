package com.roadmaps.Roadmaps.modules.authentication.controller;

import com.roadmaps.Roadmaps.common.utils.ApiResponse;
import com.roadmaps.Roadmaps.modules.authentication.dtos.request.LoginRequestDto;
import com.roadmaps.Roadmaps.modules.authentication.dtos.request.SignupRequestDto;
import com.roadmaps.Roadmaps.modules.authentication.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth/")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<?> loginWithEmailAndPassword(
            HttpServletResponse response, @Valid @ModelAttribute LoginRequestDto loginDto
    ) {
        ApiResponse<?> loginResponse = authService.login(response, loginDto);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("sign-up")
    public ResponseEntity<?> signUpWithEmailAndPassword(
            @Valid @ModelAttribute SignupRequestDto  signupDto
    ) {
        ApiResponse<?> signupResponse = authService.signup(signupDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(signupResponse);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<?>> verifyEmail(
            @RequestParam("email") @NotBlank(message = "Invalid Link!") String email,
            @RequestParam("token") @NotBlank(message = "Invalid link!") String token
    ) {
        authService.verifyEmail(email, token);

        ApiResponse<?> apiResponse = ApiResponse.success(
                null,
                "Email verified successfully."
        );

        return ResponseEntity.ok(apiResponse);
    }
}

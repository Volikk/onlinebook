package com.example.demo.controller;

import com.example.demo.dto.UserRegistrationRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "Endpoints for user authentication and registration"
)
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/registration")
    @Operation(
            summary = "Register new user",
            description = "Creates a new user account if email is not already taken"
    )
    public ResponseEntity<UserResponseDto> register(
            @Valid @RequestBody UserRegistrationRequestDto requestDto
    ) {
        UserResponseDto response = userService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

package com.example.demo.service;

import com.example.demo.dto.UserLoginRequestDto;
import com.example.demo.dto.UserLoginResponseDto;
import com.example.demo.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword()
                    )
            );
            String token = jwtUtil.generateToken(authentication.getName());

            return new UserLoginResponseDto(token);

        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid email or password", e);
        }
    }
}


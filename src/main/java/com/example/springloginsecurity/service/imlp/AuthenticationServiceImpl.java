package com.example.springloginsecurity.service.imlp;

import com.example.springloginsecurity.dao.request.SignUpRequest;
import com.example.springloginsecurity.dao.request.SigninRequest;
import com.example.springloginsecurity.dao.response.JwtAuthenticationResponse;
import com.example.springloginsecurity.entity.User;
import com.example.springloginsecurity.repository.UserRepository;
import com.example.springloginsecurity.service.AuthenticationService;
import com.example.springloginsecurity.service.JwtService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        var user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
                .email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
                .enabled(true).build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    @CircuitBreaker(name = "loginCircuitBreaker", fallbackMethod = "loginFallback")
    public JwtAuthenticationResponse signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        Map<String, Object> map = user.getRoles().stream()
                .collect(Collectors.toMap( role -> String.valueOf(role.getId()), role -> role));
        var jwt = jwtService.generateToken(map,user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    // Fallback method
    public JwtAuthenticationResponse loginFallback(SigninRequest signinRequest, Throwable ex) {
        return JwtAuthenticationResponse.builder()
                .token("Your account is locked. Please try again later.")
                .build();
    }
}

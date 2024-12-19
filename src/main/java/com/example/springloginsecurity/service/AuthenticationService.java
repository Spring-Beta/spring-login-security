package com.example.springloginsecurity.service;

import com.example.springloginsecurity.dao.request.SignUpRequest;
import com.example.springloginsecurity.dao.request.SigninRequest;
import com.example.springloginsecurity.dao.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}

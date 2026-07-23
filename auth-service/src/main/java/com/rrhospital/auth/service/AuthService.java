package com.rrhospital.auth.service;

import com.rrhospital.auth.dto.LoginRequest;
import com.rrhospital.auth.dto.LoginResponse;
import com.rrhospital.auth.dto.RegisterRequest;
import com.rrhospital.auth.dto.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}

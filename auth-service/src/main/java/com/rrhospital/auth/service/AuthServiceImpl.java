package com.rrhospital.auth.service;

import com.rrhospital.auth.dto.LoginRequest;
import com.rrhospital.auth.dto.LoginResponse;
import com.rrhospital.auth.dto.RegisterRequest;
import com.rrhospital.auth.dto.RegisterResponse;
import com.rrhospital.auth.entity.Role;
import com.rrhospital.auth.entity.User;
import com.rrhospital.auth.repository.RoleRepository;
import com.rrhospital.auth.repository.UserRepository;
import com.rrhospital.auth.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

//    public AuthServiceImpl(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
    public RegisterResponse register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        Role patientRole = roleRepository.findByName("PATIENT")
                .orElseThrow(() -> new RuntimeException("PATIENT role not found"));

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setEnabled(true);

        Set<Role> roles = new HashSet<>();
        roles.add(patientRole);

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                    "User Registered Successfully",
                savedUser.getId()
        );
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        // Check whether user exists
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid Email"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        // Generate JWT
        String token = jwtUtil.generateToken(user.getEmail());

        // Return response
        return new LoginResponse(
                token,
                "Login Successful"
        );
    }


}

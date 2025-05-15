package com.rca.myspringsecurity.service;

import com.rca.myspringsecurity.dto.JwtResponse;
import com.rca.myspringsecurity.dto.LoginRequest;
import com.rca.myspringsecurity.dto.SignupRequest;
import com.rca.myspringsecurity.exception.ResourceAlreadyExistsException;
import com.rca.myspringsecurity.model.ERole;
import com.rca.myspringsecurity.model.User;
import com.rca.myspringsecurity.repository.UserRepository;
import com.rca.myspringsecurity.security.jwt.JwtUtils;
import com.rca.myspringsecurity.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public User registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new ResourceAlreadyExistsException("Email is already in use");
        }

        if (userRepository.existsByPhone(signupRequest.getPhone())) {
            throw new ResourceAlreadyExistsException("Phone number is already in use");
        }

        if (userRepository.existsByNationalId(signupRequest.getNationalId())) {
            throw new ResourceAlreadyExistsException("National ID is already in use");
        }

        ERole role = ERole.ROLE_CUSTOMER;
        if ("ROLE_ADMIN".equalsIgnoreCase(signupRequest.getRole())) {
            role = ERole.ROLE_ADMIN;
        }

        User user = User.builder()
                .names(signupRequest.getNames())
                .email(signupRequest.getEmail())
                .phone(signupRequest.getPhone())
                .nationalId(signupRequest.getNationalId())
                .password(encoder.encode(signupRequest.getPassword()))
                .role(role)
                .build();

        return userRepository.save(user);
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(item -> item.getAuthority())
                .orElse("");

        return JwtResponse.builder()
                .token(jwt)
                .id(userDetails.getId())
                .names(userDetails.getNames())
                .email(userDetails.getEmail())
                .role(role)
                .build();
    }
}

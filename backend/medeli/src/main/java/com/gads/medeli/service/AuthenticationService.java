package com.gads.medeli.service;

import com.gads.medeli.entity.User;
import com.gads.medeli.model.request.LoginRequest;
import com.gads.medeli.model.request.RegisterRequest;
import com.gads.medeli.model.response.AuthenticationResponse;
import com.gads.medeli.repository.UserRepository;
import com.gads.medeli.security.jwt.JwtService;
import com.gads.medeli.security.validation.EmailValidator;
import com.gads.medeli.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private EmailValidator emailValidator;

    public AuthenticationResponse register(RegisterRequest request) {
       /* boolean isValidEmail = emailValidator.validateEmail(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }*/

        if (isEmailExist(request.getEmail())) {
            // TODO check of attributes are the same and
            // TODO if email not confirmed send confirmation email.

            throw new IllegalStateException("email already taken");
        }
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("Registration successful")
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) {

        var user = findUserByEmail(request.getEmail());
        var jwtToken = jwtService.generateToken(user);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("Login successful")
                .build();
    }

    boolean isEmailExist(String email) {
        return repository.findByEmail(email).isPresent();
    }

    boolean isPasswordExist(String password) {
        return repository.findByPassword(password).isPresent();
    }

    boolean isEmailAndPasswordExist(String email, String password) {
        return repository.findByPasswordAndEmail(email, password).isPresent();
    }

    User findUserByEmail(String email) {
        return repository.findByEmail(email).orElseThrow();
    }
}

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
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    private EmailValidator emailValidator;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        boolean isValidEmail = emailValidator.validateEmail(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }

        if (findByEmail(request.getEmail())) {
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
                    .build();
    }

    public AuthenticationResponse authenticate(LoginRequest request) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                ));
        if (authenticate.isAuthenticated()) {
            var user = repository.findByEmail(request.getEmail()).orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .message("Success")
                    .build();
        }
        return AuthenticationResponse.builder().message("Error whiles logging in").build();
    }

    boolean findByEmail(String email){
        return repository.findByEmail(email).isPresent();
    }
}

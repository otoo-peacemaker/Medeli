package com.gads.medeli.service;

import com.gads.medeli.entity.ConfirmationToken;
import com.gads.medeli.entity.User;
import com.gads.medeli.model.request.LoginRequest;
import com.gads.medeli.model.request.RegisterRequest;
import com.gads.medeli.model.response.AuthenticationResponse;
import com.gads.medeli.repository.UserRepository;
import com.gads.medeli.security.jwt.JwtService;
import com.gads.medeli.security.validation.EmailValidator;
import com.gads.medeli.util.EmailSender;
import com.gads.medeli.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailSender emailSender;
    private final ApplicationUserDetailService appUserDetailsService;
    private EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;


    public AuthenticationResponse register(RegisterRequest request) {
       /* boolean isValidEmail = emailValidator.validateEmail(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }*/

        if (appUserDetailsService.isEmailExist(request.getEmail())) {
            // TODO check of attributes are the same and
            // TODO if email not confirmed send confirmation email.

         //
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

        //TODO: GENERATE TOKEN,SAVE AND SEND TOKEN TO EMAIL
        String token = UUID.randomUUID().toString();
        confirmationTokenService.saveConfirmationToken(new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user));
        String link = "http://localhost:8081/api/v1/user/confirm?token=" +token;
        emailSender.send(user.getEmail(), emailSender.buildEmail(user.getFirstname(), link));

        return AuthenticationResponse.builder()
                .token(appUserDetailsService.generateJwtTokenByEmail(user.getEmail()))
                .confirmationToken(token)
                .message("Registration successful, please check your email for account activation")
                .build();
    }
    @Transactional
    public String confirmAccountByToken(String token){
        //TODO :
        // 1. generate token by the user email
        // 2. send token to email
        // 3. validate token by claim, token and email
        // 4. activate user if token is valid
        // Verify the JWT and parse it
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }
        // If we reach this point, the token is valid
        //set token
        // Activate the user's account
        confirmationTokenService.setConfirmedAt(token);
        appUserDetailsService.enableApplicationUser(confirmationToken.getAppUser().getEmail());
        return "confirm";
    }

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()));
        return AuthenticationResponse.builder()
                .token(appUserDetailsService.generateJwtTokenByEmail(request.getEmail()))
                .message("Login successful")
                .build();
    }
}

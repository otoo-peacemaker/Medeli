package com.gads.medeli.service;

import com.gads.medeli.entity.ConfirmationToken;
import com.gads.medeli.entity.User;
import com.gads.medeli.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

    public Optional<User> getUserByPasswordResetToken(final String token) {
        return Optional.ofNullable(confirmationTokenRepository.findByToken(token).get().getAppUser());
    }
}

package com.gads.medeli.util;

public interface EmailSender {
    void send(String to, String email);

    String buildEmail(String name, String link);
}

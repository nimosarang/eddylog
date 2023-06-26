package com.eddylog.api.crypto;

import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {


    public String encrypt(String rawPassword) {
        return rawPassword;
    }

    public boolean matches(String rawPassword, String encryptedPassword) {
        return rawPassword.equals(encryptedPassword);
    }
}

package com.proyecto.api.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwordPlano = "123456";
        String hash = encoder.encode(passwordPlano);
        System.out.println("HASH: " + hash);
    }
}
package com.transporte.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class GeneradorUtil {

    private static final String CARACTERES_PASSWORD = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generarPassword() {
        StringBuilder password = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            password.append(CARACTERES_PASSWORD.charAt(RANDOM.nextInt(CARACTERES_PASSWORD.length())));
        }
        return password.toString();
    }

    public String generarApiKey() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public String generarLogin(String nombres, String identificacion) {
        if (nombres == null || nombres.isEmpty()) {
            return "";
        }
        String[] partes = nombres.trim().split("\\s+");
        String inicial = partes[0].substring(0, 1).toLowerCase();
        String apellido = partes.length > 1 ? partes[partes.length - 1].substring(0, 1).toLowerCase() : "";
        return inicial + apellido + identificacion;
    }
}
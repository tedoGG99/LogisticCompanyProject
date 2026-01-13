/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbu.logistics.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author tedi
 */
public class CustomPE implements PasswordEncoder{
    @Override
    public String encode(CharSequence rawPassword) {
        return hash(rawPassword.toString());
    }
    
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // Compare the hash of the input with the stored hash
        return hash(rawPassword.toString()).equals(encodedPassword);
    }
    
    private String hash(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}

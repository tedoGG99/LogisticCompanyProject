/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbu.logistics.config;

import com.nbu.logistics.data.User;
import com.nbu.logistics.repositories.UserRepository;
import com.nbu.logistics.security.CustomPE;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author tedi
 */
@Configuration
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomPE();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable for easier testing
            // 2. Public vs Protected URLs
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/register", "/login", "/css/**").permitAll() // Public
                .anyRequest().authenticated() // Everything else requires login
            )
            
            // 3. Enable Form Login (For Browser Users)
            .formLogin(form -> form
                .loginPage("/login")             // Your custom HTML
                .loginProcessingUrl("/login")    // Where the form POSTs to
                .defaultSuccessUrl("/", true)    // Redirect here on success
                .failureUrl("/login?error=true") // Redirect here on error
                .permitAll()
            )
            .httpBasic(Customizer.withDefaults());        // Enable Basic Auth Login

        return http.build();
    }

    // 3. CONNECT TO YOUR DATABASE
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // This passes the DB hash to Spring
                .roles(user.getRole())
                .disabled(!user.isEnabled())
                .build();
        };
    }
    
}

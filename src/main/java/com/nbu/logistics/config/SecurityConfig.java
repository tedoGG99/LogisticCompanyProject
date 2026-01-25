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
            .csrf(csrf -> csrf.disable()) // Keep disabled for development
            .authorizeHttpRequests(auth -> auth
                // 1. PUBLIC ACCESS
                .requestMatchers("/register", "/login", "/css/**", "/js/**", "/images/**", "/error").permitAll()                
                // 2. EMPLOYEE ONLY PAGES
                // "Employees ... register sent and received shipments" 
                // We restrict creation and reports to employees only.
                .requestMatchers("/shipments/create", "/shipments/edit/**", "/reports/**")
                    .hasAnyRole("OFFICE_EMPLOYEE", "COURIER", "ADMIN")

                // 3. GENERAL AUTHENTICATED ACCESS
                // "Every client can see the shipments..." 
                // Both Clients and Employees need access to the main list.
                .requestMatchers("/shipments", "/shipments/", "/").authenticated()
                
                // 4. CATCH ALL
                .anyRequest().authenticated()
            )
            
            // LOGIN SETUP
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/shipments", true) // Redirect to dashboard after login
                .failureUrl("/login?error=true")
                .permitAll()
            )
            
            // LOGOUT SETUP
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // CONNECT TO DATABASE
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        // Since 'User' implements 'UserDetails', we can return it directly.
        // The getAuthorities() method in your User class handles the "ROLE_" logic.
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    
}

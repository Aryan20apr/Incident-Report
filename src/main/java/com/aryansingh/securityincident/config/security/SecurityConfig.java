package com.aryansingh.securityincident.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfig(CustomAuthenticationEntryPoint restAuthenticationEntryPoint,
            CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(customizer->customizer.authenticationEntryPoint(restAuthenticationEntryPoint))
                .exceptionHandling(customizer->customizer
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(restAuthenticationEntryPoint))
                .csrf(AbstractHttpConfigurer::disable);
        http.headers().frameOptions().disable();
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Define users and roles using in-memory authentication
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("password123"))
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


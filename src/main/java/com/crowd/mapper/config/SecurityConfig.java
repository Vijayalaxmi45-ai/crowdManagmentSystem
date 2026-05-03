package com.crowd.mapper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Admin secret key — required to register as Admin
    public static final String ADMIN_SECRET_KEY = "CROWD@ADMIN#2025";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        var manager = new InMemoryUserDetailsManager();
        // 3 users with 3 roles
        manager.createUser(User.withUsername("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN").build());
        manager.createUser(User.withUsername("user")
                .password(encoder.encode("user123"))
                .roles("USER").build());
        manager.createUser(User.withUsername("staff")
                .password(encoder.encode("staff123"))
                .roles("STAFF").build());
        return manager;
    }

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(customAuthenticationProvider)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/verify/**", "/report", "/admin/**", "/logout")
            )
            .authorizeHttpRequests(auth -> auth
                // Public pages — no login needed
                .requestMatchers("/", "/about", "/contact", "/login",
                                 "/css/**", "/js/**", "/images/**").permitAll()
                // Admin-only pages
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Report-only for Citizens
                .requestMatchers("/report", "/report/**").hasRole("USER")
                // Verification for Staff and Admin
                .requestMatchers("/verify/**").hasAnyRole("STAFF", "ADMIN")
                // All other pages require login
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/logout", "GET"))
                .logoutSuccessUrl("/?loggedout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .headers(headers -> headers.frameOptions(f -> f.disable()));

        return http.build();
    }
}

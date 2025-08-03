package com.example.hackathon;

import com.example.hackathon.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf().disable() 
            .authorizeHttpRequests(authorize ->
                    authorize
                            // Permit access to static resources like CSS, JS, and images
                            .requestMatchers("/css/", "/js/", "/assets/", "/static/").permitAll()
     
                            // Allow unauthenticated access to these paths
                            .requestMatchers("/", "/api/auth/login", "/api/auth/register", "/api/auth/perform_register","/api/bids/","/api/projects/freelancer/").permitAll()
                         // Authenticated access for profile
                            .requestMatchers("/profile/","/api/projects/").authenticated()
                            // Deny all other unauthenticated requests
                            .anyRequest().authenticated()
            )
            .formLogin(form -> form
                    .loginPage("/api/auth/login")
                    .loginProcessingUrl("/api/auth/perform_login")
                    .defaultSuccessUrl("/api/projects/dashboard", true)  // Default to profile after successful login
                    .permitAll()
            )
            .logout(logout -> logout
                    .logoutUrl("/api/auth/logout")
                    .logoutSuccessUrl("/api/auth/login?logout")
                    .permitAll()
            )
            .exceptionHandling()
            .accessDeniedPage("/access-denied");

        return http.build();
    }


    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(myUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

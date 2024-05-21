package com.example.tasktracker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                            ReactiveAuthenticationManager authenticationManager) {

                http
                        .authorizeExchange(
                            auth -> {
                                auth.anyExchange().authenticated();
                            }
                        )
                        .httpBasic(Customizer.withDefaults())
                        .csrf(ServerHttpSecurity.CsrfSpec::disable)
                        .authenticationManager(authenticationManager);

                return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        var reactiveAuthenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);

        reactiveAuthenticationManager.setPasswordEncoder(passwordEncoder);

        return reactiveAuthenticationManager;
    }
}

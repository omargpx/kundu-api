package com.citse.kunduApp.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.citse.kunduApp.utils.models.Permission.*;
import static com.citse.kunduApp.utils.models.Role.*;
import static org.springframework.http.HttpMethod.DELETE;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf()
                .disable().authorizeHttpRequests()
                .requestMatchers("/",
                        "/api/auth/**",
                        "/avatars",
                        "/info"
                )
                .permitAll()

                //endpoint protect, only access for role permission
                .requestMatchers("/api/management/**").hasAnyRole(ADMIN.name())

                .requestMatchers(DELETE, "/api/members/**").hasAuthority(TUTOR_DELETE.name())
                // protect endpoints in test, only accessible for developer user
                .anyRequest()
                .authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
        return http.build();
    }
}

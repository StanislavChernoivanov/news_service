package com.example.newsService.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) throws Exception {
        var authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.userDetailsService(userDetailsService);

        var authProvider = new DaoAuthenticationProvider(passwordEncoder);
        authProvider.setUserDetailsService(userDetailsService);

        authManagerBuilder.authenticationProvider(authProvider);

        return authManagerBuilder.build();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http
            , AuthenticationManager authenticationManager) throws Exception {
        http.authorizeHttpRequests(auth ->
                auth.requestMatchers(HttpMethod.GET, "/api/user/")
                        .hasRole("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/user/").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/user/**")
                        .hasAnyRole("ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_USER")
                        .requestMatchers(HttpMethod.PUT, "/api/user/**")
                        .hasAnyRole("ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_USER")
                        .requestMatchers(HttpMethod.GET, "/api/user/**")
                        .hasAnyRole("ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_USER")
                        .requestMatchers("/api/news/**")
                        .hasAnyRole("ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_USER")
                        .requestMatchers(HttpMethod.GET, "/api/news_category/**")
                        .hasAnyRole("ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_USER")
                        .requestMatchers(HttpMethod.POST, "/api/news_category/**")
                        .hasAnyRole("ROLE_ADMIN", "ROLE_MODERATOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/news_category/**")
                        .hasAnyRole("ROLE_ADMIN", "ROLE_MODERATOR")
                        .requestMatchers("/api/comment/**")
                        .hasAnyRole("ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_USER")
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager);

        return http.build();
    }
}

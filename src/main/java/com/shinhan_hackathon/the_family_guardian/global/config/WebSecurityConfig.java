package com.shinhan_hackathon.the_family_guardian.global.config;

import com.shinhan_hackathon.the_family_guardian.global.auth.service.UserDetailsServiceImpl;
import com.shinhan_hackathon.the_family_guardian.global.auth.service.handler.AuthenticationSuccessHandlerImpl;
import com.shinhan_hackathon.the_family_guardian.global.filter.LoginAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    public static final String LOGIN_REQUEST_URL = "/auth/login";
    public static final String INVALID_SESSION_REDIRECT_URL = "/login";

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsServiceImpl userDetailsServiceImpl, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder);
        AuthenticationManager authenticationManager = authManagerBuilder.build();

        http.
                authenticationManager(authenticationManager);

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .formLogin(formLogin -> formLogin.disable());

        http
                .headers(header -> header.frameOptions(fo -> fo.disable()).disable())
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
                    session.invalidSessionUrl(INVALID_SESSION_REDIRECT_URL);
                });

        http
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/auth/**", "/favicon.ico", "/error").permitAll();
                    request.anyRequest().authenticated();
                });

        http
                .addFilterAt(
                        abstractAuthenticationProcessingFilter(authenticationManager),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    AbstractAuthenticationProcessingFilter abstractAuthenticationProcessingFilter(AuthenticationManager authenticationManager) {
        return new LoginAuthenticationFilter(LOGIN_REQUEST_URL, authenticationManager, securityContextRepository(), authenticationSuccessHandler());
    }

    AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandlerImpl();
    }

    SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new HttpSessionSecurityContextRepository(),
                new RequestAttributeSecurityContextRepository()
        );
    }
}

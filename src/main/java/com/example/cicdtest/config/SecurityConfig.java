package com.example.cicdtest.config;

import com.example.cicdtest.common.jwt.JwtAuthenticationEntryPoint;
import com.example.cicdtest.common.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(httpBasic -> httpBasic.disable())
                // token을 사용하는 방식이기 때문에 csrf를 disable 처리한다.
                .csrf(csrf -> csrf.disable())
                // CORS 설정
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                // 요청 권한 설정
                .authorizeHttpRequests(authorize
                        -> authorize

                        .requestMatchers(
                                "/user/checkId",
                                "/user/register",
                                "/user/login").permitAll()
                        .requestMatchers("/api/user/**").permitAll()
                        .requestMatchers("/user/**").permitAll()
                        .requestMatchers("/api/runnings/create").hasRole("USER")
                        .requestMatchers("/api/runnings/delete/**").hasRole("USER")
                        .requestMatchers("/api/runnings/users/**").permitAll()
                        .requestMatchers("/api/runnings/get/**").permitAll()
                )

                // 세션을 사용하지 않기 때문에 STATELESS로 설정한다.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 예외 처리 설정 (JWT 인증 설정)
                .exceptionHandling(excep -> excep.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}




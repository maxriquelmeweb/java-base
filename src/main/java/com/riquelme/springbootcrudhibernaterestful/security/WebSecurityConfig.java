package com.riquelme.springbootcrudhibernaterestful.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.riquelme.springbootcrudhibernaterestful.exceptions.handler.CustomAccessDeniedHandler;
import com.riquelme.springbootcrudhibernaterestful.security.filter.JwtAuthenticationFilter;
import com.riquelme.springbootcrudhibernaterestful.security.filter.JwtValidationFilter;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                .requestMatchers(HttpMethod.GET, "/api/users").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/roles").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/roles/{id}").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/roles").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/roles/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/roles/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/users/{userId}/roles").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/users/{userId}/roles").hasRole("ADMIN")
                .anyRequest().authenticated())
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), messageSource))
                .addFilter(new JwtValidationFilter(authenticationManager(), messageSource))
                .exceptionHandling(ex -> ex.accessDeniedHandler(new CustomAccessDeniedHandler(messageSource)))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return http.build();
        /* .anyRequest().permitAll()) */
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(
                new CorsFilter(corsConfigurationSource()));
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsBean;
    }
}

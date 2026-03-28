package com.hrms.security;


import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.hrms.users.JwtFilter.JwtFilter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())   // ENABLE CORS
            
			.sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
	
            .authorizeHttpRequests(auth -> auth

            	    // Public endpoints
            	    .requestMatchers("/api/auth/**").permitAll()

            	    // ADMIN full control
            	    .requestMatchers("/api/admin/**").hasRole("ADMIN")

            	    // Employee management
            	    .requestMatchers("/api/employees/add")
            	        .hasAnyRole("ADMIN", "HR")

            	    .requestMatchers("/api/employees/delete/**")
            	        .hasAnyRole("ADMIN", "HR")

            	    .requestMatchers("/api/employees/all")
            	        .hasAnyRole("ADMIN", "HR", "MANAGER", "TEAM_LEAD")

            	    // Payroll section
            	    .requestMatchers("/api/payroll/**")
            	        .hasAnyRole("ADMIN", "PAYROLL_EXECUTIVE")

            	    // IT Support section
            	    .requestMatchers("/api/it/**")
            	        .hasAnyRole("ADMIN", "IT_SUPPORT")
            	        
            	     // Attendance section
            	        .requestMatchers("/api/attendance/check-in", "/api/attendance/check-out")
            	            .hasRole("EMPLOYEE")
            	        .requestMatchers("/api/attendance/manual-checkout")
            	            .hasAnyRole("HR", "ADMIN")

            	        .requestMatchers("/api/attendance/hr-report", "/api/attendance/dashboard-summary")
            	            .hasAnyRole("HR", "ADMIN")

            	        .requestMatchers("/api/attendance/summary", "/api/attendance/monthly-summary")
            	            .authenticated()
            	        .requestMatchers("/api/attendance/**")
            	            .authenticated()

            	    // Everything else requires login
            	    .anyRequest().authenticated()
            	)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
 // ✅ CORS CONFIGURATION
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        // configuration.setAllowedOrigins(List.of(
        //     "http://localhost:4200",
        //     "http://127.0.0.1:4200",
        //     "http://127.0.0.1:58217"));
//         configuration.setAllowedOrigins(List.of(
//     "http://localhost:4200",
//     "http://127.0.0.1:4200",
//     "http://127.0.0.1:30001", // Matches your Frontend NodePort
//     "http://192.168.49.2:30001" // Matches typical Minikube IP + NodePort
// ));
		configuration.setAllowedOriginPatterns(List.of("*"));
		// configuration.setAllowedOriginPatterns(List.of(
        //    "http://localhost:4200",
        //    "http://192.168.*.*",
        //    "http://127.0.0.1:*"
        // ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
    

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
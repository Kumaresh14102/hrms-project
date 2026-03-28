package com.hrms.users.JwtFilter;

import com.hrms.users.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Allow preflight requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // String path = request.getRequestURI();

        // // allow login & register without token
        // if (path.startsWith("/api/auth")) {
        //     filterChain.doFilter(request, response);
        //     return;
        // }
        

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        	filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
        	String email = jwtUtil.getEmailFromToken(token);
        	String role = jwtUtil.getRoleFromToken(token);
        	
        	// Convert role into Spring format
        	// List<GrantedAuthority> authorities =
        	//         List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
            String finalRole = role.toUpperCase().startsWith("ROLE_") ? role.toUpperCase() : "ROLE_" + role.toUpperCase();
List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(finalRole));
        	
            //  THIS PART WAS MISSING
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            // List.of() // roles can be added later
                            authorities
                    );
            authentication.setDetails(new org.springframework.security.web.authentication.WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
        	SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

}
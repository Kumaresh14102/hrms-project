package com.hrms.users.AuthController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.hrms.users.dto.LoginRequest;
import com.hrms.users.dto.RegisterRequest;
import com.hrms.users.dto.JwtResponse;
import com.hrms.users.entity.User;
import com.hrms.users.repository.UserRepository;
import com.hrms.users.util.JwtUtil;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail());

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }

        //String token = jwtUtil.generateToken(user.getEmail());
        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return ResponseEntity.ok(JwtResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .build());
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: Email is already in use!");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        //user.setPassword(request.getPassword()); // plain text for now
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);

        return ResponseEntity.ok("User created successfully");
    }

}

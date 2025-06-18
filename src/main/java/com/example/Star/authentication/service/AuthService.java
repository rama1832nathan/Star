package com.example.Star.authentication.service;

import com.example.Star.authentication.dto.LoginRequest;
import com.example.Star.authentication.dto.RegisterRequest;
import com.example.Star.authentication.model.PasswordHistory;
import com.example.Star.authentication.model.User;
import com.example.Star.authentication.repo.PasswordHistoryRepository;
import com.example.Star.authentication.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired private PasswordHistoryRepository passwordHistoryRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public String register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "User already exists";
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(hashedPassword);
        user.setRole(request.getRole());

        userRepository.save(user);
        savePasswordHistory(request.getUsername(), hashedPassword);

        return "User registered successfully";
    }

    private void savePasswordHistory(String username, String hashedPassword) {
        PasswordHistory history = new PasswordHistory();
        history.setUsername(username);
        history.setHashedPassword(hashedPassword);
        history.setChangedAt(LocalDateTime.now());
        passwordHistoryRepository.save(history);
    }

    public String login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) return "User not found";

        User user = userOpt.get();
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return "Login successful";
        }

        return "Invalid credentials";
    }
}


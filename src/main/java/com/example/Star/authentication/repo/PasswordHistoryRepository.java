package com.example.Star.authentication.repo;

import com.example.Star.authentication.model.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
    List<PasswordHistory> findTop3ByUsernameOrderByChangedAtDesc(String username);
}


package com.example.repository;

import com.example.enums.AccountStatus;
import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByIdAndStatus(Long id, AccountStatus status);
}

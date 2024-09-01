package com.syshero.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syshero.authservice.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
}

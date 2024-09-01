package com.syshero.accountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syshero.accountservice.entity.Account;


public interface AccountRepository extends JpaRepository<Account, Long> {

}

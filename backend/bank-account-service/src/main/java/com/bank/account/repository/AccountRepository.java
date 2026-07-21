package com.bank.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.account.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer>{

	boolean existsByAccountNumber(String number);

}

package com.bank.transaction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.transaction.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
	
	//List<Transaction> findByAccountIdOrderByTransactionTimeDesc(Integer accountId);
	
	Optional<Transaction> findByReferenceNumber(String referenceNumber);

	List<Transaction> findByAccountIdOrderByTransactionTimeDesc(Integer accountId);

}

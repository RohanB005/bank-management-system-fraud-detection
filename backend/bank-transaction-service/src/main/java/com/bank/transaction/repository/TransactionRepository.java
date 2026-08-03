package com.bank.transaction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.transaction.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
	
	//List<Transaction> findByAccountIdOrderByTransactionTimeDesc(Integer accountId);
	
	Optional<Transaction> findByReferenceNumber(String referenceNumber);

	List<Transaction> findByAccountIdOrderByTransactionTimeDesc(Integer accountId);

}

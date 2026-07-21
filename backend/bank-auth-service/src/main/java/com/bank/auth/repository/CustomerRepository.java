package com.bank.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.auth.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
	Optional<Customer> findByEmail(String email);
	
	Optional<Customer> findByMobile(String mobile);
	
	boolean existsByEmail(String email);
	
	boolean existsByMobile(String mobile);
	
	boolean existsByAadhaar(String aadhaar);
	
	boolean existsByPan(String pan);

}

package com.bank.account.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer accountId;
	
	@Column(nullable = false)
	private Integer customerId;
	
	@Column(unique = true, nullable = false)
	private String accountNumber;
	
	@Enumerated(EnumType.STRING)
	private AccountType accountType;
	
	private BigDecimal balance = BigDecimal.ZERO;
	
	private String branchName;
	private String ifscCode;
	
	@Enumerated(EnumType.STRING)
	private AccountStatus status = AccountStatus.Active;
	
	private LocalDate openDate = LocalDate.now();
	
	public enum AccountType {Savings, Current, Salary}
	public enum AccountStatus {Active, Inactive, Blocked, Closed }
	
	
}

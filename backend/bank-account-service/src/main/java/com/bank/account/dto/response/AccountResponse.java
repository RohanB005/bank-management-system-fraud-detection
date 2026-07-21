package com.bank.account.dto.response;

import java.math.BigDecimal;

import com.bank.account.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
	private Integer accountId;
	private Integer customerId;
	private String accountNumber;
	private String accountType;
	private BigDecimal balance;
	private String branchName;
	private String ifscCode;
	private String status;
	
	
	
	public static AccountResponse fromEntity(Account account) {
		return new AccountResponse(
				account.getAccountId(),
				account.getCustomerId(),
				account.getAccountNumber(),
				account.getAccountType().name(),
				account.getBalance(),
				account.getBranchName(),
				account.getIfscCode(),
				account.getStatus().name()
				);
	}

}

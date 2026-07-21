package com.bank.account.dto.request;

import com.bank.account.entity.Account.AccountType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAccountRequest {

	@NotNull(message ="customerId is required")
	private Integer customerId;
	
	@NotNull(message = "accountType is required")
	private AccountType accountType;
	
	@NotNull(message = "branchname is required")
	private String branchName;
	
	@NotNull(message = "ifscCode is required")
	private String ifscCode;
}

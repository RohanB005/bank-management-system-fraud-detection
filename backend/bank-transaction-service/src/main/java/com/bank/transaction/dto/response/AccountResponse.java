package com.bank.transaction.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
	
	private Integer accountId;
	
	private String accountNumber;
	
	private BigDecimal balance;
	
	private String accountType;
	
	private String status;

	public BigDecimal getBalance() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

}

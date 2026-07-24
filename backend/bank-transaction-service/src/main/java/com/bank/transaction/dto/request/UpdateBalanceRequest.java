package com.bank.transaction.dto.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBalanceRequest {
	
	public UpdateBalanceRequest(BigDecimal updatedBalance) {
		// TODO Auto-generated constructor stub
	}

	private BigDecimal balance;

}

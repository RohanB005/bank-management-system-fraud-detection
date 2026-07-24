package com.bank.transaction.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WithdrawRequest {

	@NotNull
	private Integer accountId;
	
	@NotNull
	@DecimalMin(value = "0.01")
	private BigDecimal amount;
	
	private String description;
	
	@NotBlank
	private String transactionCity;

	public Integer getAccountId() {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal getAmount() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTransactionCity() {
		// TODO Auto-generated method stub
		return null;
	}
}

package com.bank.account.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse {

	 private Integer fromAccountId;
	    private Integer toAccountId;
	    private BigDecimal fromAccountBalance;
	    private BigDecimal toAccountBalance;
	    private String status;
}

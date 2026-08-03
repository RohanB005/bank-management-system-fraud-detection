package com.bank.account.dto.request;

import com.bank.account.entity.Account.AccountType;
import lombok.Data;

@Data
public class UpdateAccountRequest {
	private AccountType accountType;
}

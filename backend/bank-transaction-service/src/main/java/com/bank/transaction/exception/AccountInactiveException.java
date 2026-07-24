package com.bank.transaction.exception;

public class AccountInactiveException extends RuntimeException {
	
	public AccountInactiveException(String message) {
		super(message);
	}
}

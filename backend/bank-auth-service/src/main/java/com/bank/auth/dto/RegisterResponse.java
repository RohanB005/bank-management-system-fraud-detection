package com.bank.auth.dto;

public class RegisterResponse {
	private Long customerId;
	private String message;
	
	public RegisterResponse() {
		
	}
	public RegisterResponse(Long customerId, String message) {
		this.customerId = customerId;
		this.message = message;
		
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}

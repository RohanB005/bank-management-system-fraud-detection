package com.bank.auth.dto;

import jakarta.validation.constraints.*;

public class RegisterRequest {
	
	@NotBlank(message = "Name is required")
	private String name;
	
	@Email(message = "Invalid Email")
	@NotBlank
	private String email;
	
	@Pattern(regexp = "^[6-9]\\d{9}$",
			message = "Invalid Mobile Number")
	private String mobile;
	
	@Size(min = 8, message = "Password should contain minimum 8 characters")
	private String password;
	
	@Pattern(regexp = "\\d{12}",
			message = "Aadhaar should contain 12 digits")
	private String aadhaar;
	
	@Pattern(regexp = "[A-Z]{5}{0-9}{4}[A-Z]{1}",
			message = "Invalid PAN")
	private String pan;
	
	@NotBlank
	private String address;
	
	public RegisterRequest() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAadhaar() {
		return aadhaar;
	}

	public void setAadhaar(String aadhaar) {
		this.aadhaar = aadhaar;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
	

}

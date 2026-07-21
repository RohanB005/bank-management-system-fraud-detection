package com.bank.auth.service;

import com.bank.auth.dto.RegisterRequest;

public interface AuthService {
	
	String register(RegisterRequest request);

}

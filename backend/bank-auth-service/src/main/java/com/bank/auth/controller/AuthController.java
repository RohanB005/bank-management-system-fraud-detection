package com.bank.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.bank.auth.dto.VerifyOtpRequest;
import com.bank.auth.dto.VerifyOtpResponse;
import com.bank.auth.dto.ForgotPasswordRequest;
import com.bank.auth.dto.ForgotPasswordResponse;
import com.bank.auth.dto.LoginRequest;
import com.bank.auth.dto.LoginResponse;
import com.bank.auth.dto.LogoutResponse;
import com.bank.auth.dto.RegisterRequest;
import com.bank.auth.dto.RegisterResponse;
import com.bank.auth.dto.ResetPasswordRequest;
import com.bank.auth.dto.ResetPasswordResponse;
import com.bank.auth.dto.VerifyOTPRequest;
import com.bank.auth.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request){
		RegisterResponse response = authService.register(request);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PostMapping("/verify-otp")
	public ResponseEntity<VerifyOtpResponse> verifyOtp(
	        @Valid @RequestBody VerifyOTPRequest request) {

	    VerifyOtpResponse response = authService.verifyOtp(request);

	    return ResponseEntity.ok(response);
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(
	        @Valid @RequestBody LoginRequest request) {

	    LoginResponse response = authService.login(request);

	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/profile")
	public ResponseEntity<String> profile() {
	    return ResponseEntity.ok("Welcome! JWT Authentication Successful.");
	}
	
	@PostMapping("/forgot-password")
	public ResponseEntity<ForgotPasswordResponse> forgotPassword(
	        @Valid @RequestBody ForgotPasswordRequest request) {

	    ForgotPasswordResponse response =
	            authService.forgotPassword(request);

	    return ResponseEntity.ok(response);
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<ResetPasswordResponse> resetPassword(
	        @Valid @RequestBody ResetPasswordRequest request) {

	    ResetPasswordResponse response =
	            authService.resetPassword(request);

	    return ResponseEntity.ok(response);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<LogoutResponse> logout() {

	    LogoutResponse response = authService.logout();

	    return ResponseEntity.ok(response);
	}
	

}

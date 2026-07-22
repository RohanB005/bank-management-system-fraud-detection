package com.bank.auth.serviceimpl;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.auth.config.JwtUtil;
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
import com.bank.auth.dto.VerifyOtpResponse;
import com.bank.auth.entity.Customer;
import com.bank.auth.entity.OTP;
import com.bank.auth.repository.CustomerRepository;
import com.bank.auth.repository.OTPRepository;
import com.bank.auth.service.AuthService;
import com.bank.auth.util.OtpGenerator;

@Service
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final OTPRepository otpRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    public AuthServiceImpl(CustomerRepository customerRepository,
                           OTPRepository otpRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {

        this.customerRepository = customerRepository;
        this.otpRepository = otpRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {

        // Check duplicate email
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Check duplicate mobile
        if (customerRepository.existsByMobile(request.getMobile())) {
            throw new RuntimeException("Mobile number already exists");
        }

        // Check duplicate Aadhaar
        if (customerRepository.existsByAadhaar(request.getAadhaar())) {
            throw new RuntimeException("Aadhaar already exists");
        }

        // Check duplicate PAN
        if (customerRepository.existsByPan(request.getPan())) {
            throw new RuntimeException("PAN already exists");
        }

        // Create customer
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setMobile(request.getMobile());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setAadhaar(request.getAadhaar());
        customer.setPan(request.getPan());
        customer.setAddress(request.getAddress());

        // Optional (if these fields exist)
        customer.setVerified(false);
        customer.setStatus("PENDING");

        // Save customer
        customerRepository.save(customer);

       

        return new RegisterResponse(
                customer.getCustomerId(),
                "Customer registered successfully. OTP generated."
        );
    }

    @Override
    public VerifyOtpResponse verifyOtp(VerifyOTPRequest request) {

        // Find customer
        Customer customer = customerRepository
                .findByMobile(request.getMobile())
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));

        // Find latest OTP
        OTP otp = otpRepository
                .findTopByCustomerOrderByCreatedAtDesc(customer)
                .orElseThrow(() ->
                        new RuntimeException("OTP not found"));

        // Validate OTP
        if (!otp.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        // Check expiry
        if (otp.getExpiryTime().isBefore(java.time.LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        // Check if already used
        if (otp.isUsed()) {
            throw new RuntimeException("OTP already used");
        }

        // Mark OTP as used
        otp.setUsed(true);
        otpRepository.save(otp);

        // Activate customer
        customer.setVerified(true);
        customer.setStatus("ACTIVE");
        customerRepository.save(customer);

        return new VerifyOtpResponse(
                "OTP verified successfully"
        );
    }
    @Override
    public LoginResponse login(LoginRequest request) {
    	Customer customer = customerRepository
    	        .findByEmail(request.getEmail())
    	        .orElseThrow(() ->
    	                new RuntimeException("Customer not found"));
    	
    	if (!customer.isVerified()) {
    	    throw new RuntimeException("Please verify your account first.");
    	}
    	if (!"ACTIVE".equalsIgnoreCase(customer.getStatus())) {
    	    throw new RuntimeException("Account is not active.");
    	}
    	if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
    	    throw new RuntimeException("Invalid email or password");
    	}
    	
    	String token = jwtUtil.generateToken(customer.getEmail());
    	
    	return new LoginResponse(
    			customer.getCustomerId(),
    			customer.getName(),
    			customer.getEmail(),
    			token,
    			"Login Successful"
    			);
    }
    @Override
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {
    		
    	Customer customer = customerRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));

        String generatedOtp = OtpGenerator.generateOtp();

        OTP otp = new OTP();
        otp.setCustomer(customer);
        otp.setOtp(generatedOtp);
        otp.setUsed(false);
        otp.setCreatedAt(LocalDateTime.now());
        otp.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(otp);

        System.out.println("Password Reset OTP : " + generatedOtp);

        return new ForgotPasswordResponse();
    }
    
    @Override
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {
    	
    		Customer customer = customerRepository
    				.findByEmail(request.getEmail())
    				.orElseThrow(() -> 
    						new RuntimeException("Customer not found"));
    		OTP otp = otpRepository
    	            .findTopByCustomerOrderByCreatedAtDesc(customer)
    	            .orElseThrow(() ->
    	                    new RuntimeException("OTP not found"));

    	    if (!otp.getOtp().equals(request.getOtp())) {
    	        throw new RuntimeException("Invalid OTP");
    	    }

    	    if (otp.isUsed()) {
    	        throw new RuntimeException("OTP already used");
    	    }

    	    if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
    	        throw new RuntimeException("OTP expired");
    	    }

    	    customer.setPassword(
    	            passwordEncoder.encode(request.getNewPassword()));

    	    customerRepository.save(customer);

    	    otp.setUsed(true);
    	    otpRepository.save(otp);

    	    return new ResetPasswordResponse(
    	            "Password reset successfully");
    }
    
    @Override
    public LogoutResponse logout() {

        return new LogoutResponse(
                "Logout Successful. Please remove the JWT token from client storage.");
    }
    
}
package com.bank.auth.serviceimpl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.auth.dto.RegisterRequest;
import com.bank.auth.entity.Customer;
import com.bank.auth.repository.CustomerRepository;
import com.bank.auth.repository.OTPRepository;
import com.bank.auth.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final OTPRepository otpRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthServiceImpl(CustomerRepository customerRepository,
                           OTPRepository otpRepository,
                           BCryptPasswordEncoder passwordEncoder) {

        this.customerRepository = customerRepository;
        this.otpRepository = otpRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String register(RegisterRequest request) {

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (customerRepository.existsByMobile(request.getMobile())) {
            throw new RuntimeException("Mobile number already exists");
        }

        if (customerRepository.existsByAadhaar(request.getAadhaar())) {
            throw new RuntimeException("Aadhaar already exists");
        }

        if (customerRepository.existsByPan(request.getPan())) {
            throw new RuntimeException("PAN already exists");
        }
        
        
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setMobile(request.getMobile());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setAadhaar(request.getAadhaar());
        customer.setPan(request.getPan());
        customer.setAddress(request.getAddress());
        
        return "Registration Successful";
    }
}

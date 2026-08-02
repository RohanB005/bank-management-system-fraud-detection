package com.bank.auth.serviceimpl;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.auth.config.JwtUtil;
import com.bank.auth.dto.*;
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


    public AuthServiceImpl(
            CustomerRepository customerRepository,
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


        if(customerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }


        if(customerRepository.existsByMobile(request.getMobile())) {
            throw new RuntimeException("Mobile number already exists");
        }


        if(customerRepository.existsByAadhaarNumber(request.getAadhaar())) {
            throw new RuntimeException("Aadhaar already exists");
        }


        if(customerRepository.existsByPanNumber(request.getPan())) {
            throw new RuntimeException("PAN already exists");
        }



        Customer customer = new Customer();


        customer.setFirstName(request.getFirstName());

        customer.setLastName(request.getLastName());

        customer.setDateOfBirth(request.getDateOfBirth());

        customer.setGender(request.getGender());

        customer.setEmail(request.getEmail());

        customer.setMobile(request.getMobile());


        customer.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );


        customer.setAadhaarNumber(request.getAadhaar());

        customer.setPanNumber(request.getPan());


        customer.setAddress(request.getAddress());

        customer.setCity(request.getCity());

        customer.setState(request.getState());

        customer.setPincode(request.getPincode());



        customerRepository.save(customer);



        return new RegisterResponse(
                customer.getCustomerId(),
                "Customer registered successfully"
        );

    }





    @Override
    public VerifyOtpResponse verifyOtp(VerifyOTPRequest request) {


        Customer customer =
                customerRepository.findByMobile(request.getMobile())
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));



        OTP otp =
                otpRepository
                .findTopByCustomerOrderByCreatedAtDesc(customer)
                .orElseThrow(() ->
                        new RuntimeException("OTP not found"));



        if(!otp.getOtp().equals(request.getOtp())) {

            throw new RuntimeException("Invalid OTP");
        }



        if(otp.getExpiryTime()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException("OTP expired");
        }



        otp.setUsed(true);

        otpRepository.save(otp);



        return new VerifyOtpResponse(
                "OTP verified successfully"
        );
    }





    @Override
    public LoginResponse login(LoginRequest request) {


        Customer customer =
                customerRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));



        if(!passwordEncoder.matches(
                request.getPassword(),
                customer.getPasswordHash())) {


            throw new RuntimeException(
                    "Invalid email or password");
        }



        String token =
                jwtUtil.generateToken(customer.getEmail());



        return new LoginResponse(
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                token,
                "Login Successful"
        );

    }





    @Override
    public ForgotPasswordResponse forgotPassword(
            ForgotPasswordRequest request) {


        Customer customer =
                customerRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));



        String generatedOtp =
                OtpGenerator.generateOtp();



        OTP otp = new OTP();

        otp.setCustomerId(customer);

        otp.setOtp(generatedOtp);

        otp.setUsed(false);

        otp.setCreatedAt(LocalDateTime.now());

        otp.setExpiryTime(
                LocalDateTime.now().plusMinutes(5)
        );



        otpRepository.save(otp);



        System.out.println(
                "Password Reset OTP : "
                + generatedOtp);



        return new ForgotPasswordResponse();

    }






    @Override
    public ResetPasswordResponse resetPassword(
            ResetPasswordRequest request) {


        Customer customer =
                customerRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));



        OTP otp =
                otpRepository
                .findTopByCustomerOrderByCreatedAtDesc(customer)
                .orElseThrow(() ->
                        new RuntimeException("OTP not found"));



        if(!otp.getOtp().equals(request.getOtp())) {

            throw new RuntimeException("Invalid OTP");
        }



        if(otp.getUsed()) {

            throw new RuntimeException("OTP already used");
        }



        if(otp.getExpiryTime()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException("OTP expired");
        }



        customer.setPasswordHash(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );


        customerRepository.save(customer);



        otp.setUsed(true);

        otpRepository.save(otp);



        return new ResetPasswordResponse(
                "Password reset successfully"
        );

    }





    @Override
    public LogoutResponse logout() {


        return new LogoutResponse(
                "Logout Successful. Please remove JWT token from client storage."
        );
    }


	@Override
	public boolean customerExists(Integer customerId) {
		return customerRepository.existsById(customerId);
	}

}
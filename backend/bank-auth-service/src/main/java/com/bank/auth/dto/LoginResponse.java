package com.bank.auth.dto;

public class LoginResponse {

    private Long customerId;
    private String name;
    private String email;
    private String token;
    private String message;

    // Default Constructor
    public LoginResponse() {
    }

    // Parameterized Constructor
    public LoginResponse(Long customerId, String name, String email, String token, String message) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.token = token;
        this.message = message;
    }

    // Getters and Setters

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LoginResponse [customerId=" + customerId
                + ", name=" + name
                + ", email=" + email
                + ", token=" + token
                + ", message=" + message + "]";
    }
}
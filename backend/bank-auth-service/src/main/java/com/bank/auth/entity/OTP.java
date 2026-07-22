package com.bank.auth.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "otp")
public class OTP {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "opt_id")
	private Long otpId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@Column(nullable = false)
	private String otp;
	
	@Column(name = "expiry_time")
	private LocalDateTime expiryTime;
	
	@Column(nullable = false)
	private Boolean used;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@PrePersist
	public void prePersist() {
		createdAt = LocalDateTime.now();
		expiryTime = LocalDateTime.now().plusMinutes(5);
		used = false;
	}
	public OTP() {
	}
	public Long getOtpId() {
		return otpId;
	}
	public void setOtpId(Long otpId) {
		this.otpId = otpId;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public LocalDateTime getExpiryTime() {
		return expiryTime;
	}
	public void setExpiryTime(LocalDateTime expiryTime) {
		this.expiryTime = expiryTime;
	}
	public Boolean getUsed() {
		return used;
	}
	public void setUsed(Boolean used) {
		this.used = used;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public boolean isUsed() {
		// TODO Auto-generated method stub
		return used;
	}
	

}

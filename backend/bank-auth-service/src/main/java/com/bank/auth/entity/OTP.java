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
	

}

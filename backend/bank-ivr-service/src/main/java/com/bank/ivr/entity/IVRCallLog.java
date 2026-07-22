package com.bank.ivr.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "IVRCallLog")
@Data
@NoArgsConstructor
public class IVRCallLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer callId;
	
	@Column(nullable = false)
	private Integer customerId;
	
	private String serviceType;
	
	private LocalDateTime callTime = LocalDateTime.now();
	
	private Integer duration;
}

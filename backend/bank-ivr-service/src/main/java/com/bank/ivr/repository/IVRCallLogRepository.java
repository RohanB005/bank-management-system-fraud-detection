package com.bank.ivr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.ivr.entity.IVRCallLog;

public interface IVRCallLogRepository extends JpaRepository<IVRCallLog, Integer>{

}

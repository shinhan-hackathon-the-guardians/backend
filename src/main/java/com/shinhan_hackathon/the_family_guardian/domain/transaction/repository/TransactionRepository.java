package com.shinhan_hackathon.the_family_guardian.domain.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}

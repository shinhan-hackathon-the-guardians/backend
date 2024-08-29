package com.shinhan_hackathon.the_family_guardian.domain.transaction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.Transaction;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.TransactionStatus;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	List<Transaction> findAllByStatus(TransactionStatus pending);

}

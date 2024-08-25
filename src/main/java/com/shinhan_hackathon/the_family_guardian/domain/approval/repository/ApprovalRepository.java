package com.shinhan_hackathon.the_family_guardian.domain.approval.repository;

import com.shinhan_hackathon.the_family_guardian.domain.approval.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
}

package com.shinhan_hackathon.the_family_guardian.domain.approval.repository;

import com.shinhan_hackathon.the_family_guardian.domain.approval.entity.Approval;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    boolean existsByUser(User user);
    Optional<Approval> findByUser(User user);
}

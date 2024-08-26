package com.shinhan_hackathon.the_family_guardian.domain.payment.repository;

import com.shinhan_hackathon.the_family_guardian.domain.payment.entity.PaymentLimit;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentLimitRepository extends JpaRepository<PaymentLimit, Long> {
    Optional<PaymentLimit> findByUser(User user);
}

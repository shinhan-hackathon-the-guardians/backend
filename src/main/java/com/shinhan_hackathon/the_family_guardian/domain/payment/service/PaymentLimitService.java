package com.shinhan_hackathon.the_family_guardian.domain.payment.service;

import com.shinhan_hackathon.the_family_guardian.domain.payment.entity.PaymentLimit;
import com.shinhan_hackathon.the_family_guardian.domain.payment.repository.PaymentLimitRepository;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import com.shinhan_hackathon.the_family_guardian.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentLimitService {
    private final PaymentLimitRepository paymentLimitRepository;
    private final UserRepository userRepository;

    // TODO: 전체 한도 확인
    public boolean checkMaxAmountLimit(Long userId, Long transactionBalance) {
        // Rule 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found."));
        PaymentLimit paymentLimit = paymentLimitRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("PaymentLimit Not Found."));

        Integer maxAmountLimit = paymentLimit.getMaxLimitAmount();
        Integer amount_used = paymentLimit.getAmountUsed();

        // 한도를 초과하면 False
        return (amount_used + transactionBalance) <= maxAmountLimit;
    }

     // TODO: 단건 한도 확인
    public boolean checkSingleTransactionLimit(Long userId, Long transactionBalance) {
        // Rule 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found."));
        PaymentLimit paymentLimit = paymentLimitRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("PaymentLimit Not Found."));

        Integer singleTransactionLimit = paymentLimit.getSingleTransactionLimit();

        // 단건 한도를 초과하면 False
        return transactionBalance >= singleTransactionLimit;
    }
}

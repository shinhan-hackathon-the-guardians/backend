package com.shinhan_hackathon.the_family_guardian.domain.payment.service;

import com.shinhan_hackathon.the_family_guardian.domain.payment.dto.UpdateLimitRequest;
import com.shinhan_hackathon.the_family_guardian.domain.payment.dto.UpdateLimitResponse;
import com.shinhan_hackathon.the_family_guardian.domain.payment.entity.PaymentLimit;
import com.shinhan_hackathon.the_family_guardian.domain.payment.repository.PaymentLimitRepository;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import com.shinhan_hackathon.the_family_guardian.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public UpdateLimitResponse manageMemberLimit(UpdateLimitRequest updateLimitRequest) {
        User user = userRepository.getReferenceById(updateLimitRequest.targetUserId());
        PaymentLimit paymentLimit = paymentLimitRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("결제 제한이 없습니다."));

        paymentLimit.updatePeriod(updateLimitRequest.period());
        paymentLimit.updateMaxLimitAmount(updateLimitRequest.maxLimitAmount());
        paymentLimit.updateSingleTransactionLimit(updateLimitRequest.singleTransactionLimit());

        return new UpdateLimitResponse(
                user.getId(),
                paymentLimit.getPeriod(),
                paymentLimit.getSingleTransactionLimit(),
                paymentLimit.getMaxLimitAmount()
        );
    }

    public List<PaymentLimit> findAllPaymentLimit() {
        return paymentLimitRepository.findAll();
    }
}

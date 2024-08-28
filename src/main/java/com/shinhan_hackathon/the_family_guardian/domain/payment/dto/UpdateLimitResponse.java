package com.shinhan_hackathon.the_family_guardian.domain.payment.dto;

import com.shinhan_hackathon.the_family_guardian.domain.payment.entity.LimitPeriod;

public record UpdateLimitResponse(
        Long targetUserId,
        LimitPeriod period,
        Integer singleTransactionLimit,
        Integer maxLimitAmount
) {
}

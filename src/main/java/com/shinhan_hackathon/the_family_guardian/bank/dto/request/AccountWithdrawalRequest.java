package com.shinhan_hackathon.the_family_guardian.bank.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan_hackathon.the_family_guardian.bank.dto.Header;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountWithdrawalRequest {
    @JsonProperty("Header")
    private Header header;

    private String accountNo;

    private Long transactionBalance;

    private String transactionSummary; // 출금 요약
}

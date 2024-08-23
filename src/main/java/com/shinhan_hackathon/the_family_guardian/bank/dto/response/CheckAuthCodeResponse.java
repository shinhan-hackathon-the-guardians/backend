package com.shinhan_hackathon.the_family_guardian.bank.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan_hackathon.the_family_guardian.bank.dto.HeaderResponse;

public record CheckAuthCodeResponse(
        @JsonProperty("Header")
        HeaderResponse header,

        @JsonProperty("REC")
        Rec rec
) {

    public record Rec (
            String status,
            Long transactionUniqueNo,
            String accountNo
    ) {
    }
}

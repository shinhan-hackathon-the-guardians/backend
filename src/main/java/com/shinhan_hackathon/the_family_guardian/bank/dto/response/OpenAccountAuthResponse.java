package com.shinhan_hackathon.the_family_guardian.bank.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan_hackathon.the_family_guardian.bank.dto.HeaderResponse;

import java.util.List;

public record OpenAccountAuthResponse(
        @JsonProperty("Header")
        HeaderResponse header,

        @JsonProperty("REC")
        List<Rec> rec
) {

    record Rec (
            String transactionUniqueNo,
            String accountNo
    ) {
    }
}

package com.shinhan_hackathon.the_family_guardian.bank.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan_hackathon.the_family_guardian.bank.dto.HeaderResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DemandDepositCreationResponse {
    @JsonProperty("Header")
    private HeaderResponse header;

    @JsonProperty("REC")
    private Rec rec;

    @Data
    @Builder
    private static class Rec {
        private String accountTypeUniqueNo;
        private String bankCode;
        private String bankName;
        private String accountTypeCode;
        private String accountTypeName;
        private String accountName;
        private String accountDescription;
        private String accountType;
    }
}

package com.shinhan_hackathon.the_family_guardian.bank.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan_hackathon.the_family_guardian.bank.dto.HeaderResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountCreationResponse {
    @JsonProperty("Header")
    private HeaderResponse header;

    @JsonProperty("REC")
    private Rec rec;

    @Data
    @Builder
    public static class Rec {
        private String bankCode;
        private String accountNo;
        private Currency currency;

        @Data
        @Builder
        public static class Currency {
            private String currency;
            private String currencyName;
        }
    }

}

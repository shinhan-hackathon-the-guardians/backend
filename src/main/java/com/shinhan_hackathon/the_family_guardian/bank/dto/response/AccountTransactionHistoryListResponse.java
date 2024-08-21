package com.shinhan_hackathon.the_family_guardian.bank.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan_hackathon.the_family_guardian.bank.dto.HeaderResponse;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountTransactionHistoryListResponse {
    @JsonProperty("Header")
    private HeaderResponse header;

    @JsonProperty("REC")
    private Rec rec;

    @Data
    @Builder
    public static class Rec {
        private String totalCount;
        private List<Transaction> list;

        @Data
        @Builder
        public static class Transaction {
            private String transactionUniqueNo;
            private String transactionDate;
            private String transactionTime;
            private String transactionType;
            private String transactionTypeName;
            private String transactionAccountNo;
            private String transactionBalance;
            private String transactionAfterBalance;
            private String transactionSummary;
            private String transactionMemo;
        }
    }
}

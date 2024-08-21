package com.shinhan_hackathon.the_family_guardian.bank.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan_hackathon.the_family_guardian.bank.dto.Header;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DemandDepositCreationRequest {
    @JsonProperty("Header")
    private Header header;

    private String bankCode;

    private String accountName;

    private String accountDescription;
}

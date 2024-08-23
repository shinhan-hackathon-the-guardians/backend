package com.shinhan_hackathon.the_family_guardian.bank.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan_hackathon.the_family_guardian.bank.dto.Header;

public record OpenAccountAuthRequest (
    @JsonProperty("Header")
    Header header,

    String accountNo,
    String authText
) {
}
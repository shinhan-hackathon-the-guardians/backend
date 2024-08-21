package com.shinhan_hackathon.the_family_guardian.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountListRequest {
	@JsonProperty("Header")
	private Header header;
}

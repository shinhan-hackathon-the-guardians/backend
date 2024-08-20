package com.shinhan_hackathon.the_family_guardian.bank.account;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.shinhan_hackathon.the_family_guardian.bank.dto.AccountListRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.Header;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

	// TODO: 사용자 계좌 생성
	public void createAccount() {
		RestClient restClient = RestClient.create();

//		restClient.post()


	}

	// TODO: 사용자 계좌 목록 조회
	// TODO: 반환값 정할 것
	public void inquireAccountList() {

		String apiName = "inquireDemandDepositAccountList";
		String apiServiceCode = apiName;
		String apiKey = null;
		String userKey = null;

		Header header = Header.builder()
			.apiName(apiName)
			.apiServiceCode(apiServiceCode)
			.apiKey(apiKey)
			.userKey(userKey)
			.build();

		// 최상위 객체로 감싸기
		AccountListRequest accountListRequest = AccountListRequest.builder()
			.header(header)
			.build();

		String url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit/inquireDemandDepositAccountList";
		RestClient restClient = RestClient.create();
		ResponseEntity response = restClient.post()
			.uri(url)
			.contentType(MediaType.APPLICATION_JSON)
			.body(accountListRequest)
			.retrieve()
			.toEntity(String.class);

		System.out.println(response);
	}
}

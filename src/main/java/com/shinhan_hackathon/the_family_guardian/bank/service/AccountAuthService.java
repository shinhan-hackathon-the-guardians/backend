package com.shinhan_hackathon.the_family_guardian.bank.service;

import com.shinhan_hackathon.the_family_guardian.bank.dto.Header;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.*;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.CheckAuthCodeResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.OpenAccountAuthResponse;
import com.shinhan_hackathon.the_family_guardian.bank.util.BankUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountAuthService {
    private static final String BASE_URL = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/accountAuth/";
    private final RestClient restClient = RestClient.create();
    private final BankUtil bankUtil;

    @Value("${bank.company-name}")
    private String companyAuthText;


    public OpenAccountAuthResponse openAccountAuth(String accountNo) {
        String apiName = "openAccountAuth";
        Header header = bankUtil.createHeader(apiName);
        OpenAccountAuthRequest authRequest = new OpenAccountAuthRequest(header, accountNo, companyAuthText);

        return executePost(apiName, authRequest, OpenAccountAuthResponse.class).getBody();
    }

    public CheckAuthCodeResponse checkAuthCode(String accountNo, String authCode) {
        String apiName = "checkAuthCode";
        Header header = bankUtil.createHeader(apiName);
        CheckAuthCodeRequest authRequest = new CheckAuthCodeRequest(header, accountNo, companyAuthText, authCode);

        return executePost(apiName, authRequest, CheckAuthCodeResponse.class).getBody();
    }

    private <T> ResponseEntity<T> executePost(String endpoint, Object requestBody, Class<T> responseType) {
        return restClient.post()
                .uri(BASE_URL + endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .toEntity(responseType);
    }
}

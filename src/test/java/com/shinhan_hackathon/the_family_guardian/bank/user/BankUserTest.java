package com.shinhan_hackathon.the_family_guardian.bank.user;

import com.google.gson.Gson;
import com.shinhan_hackathon.the_family_guardian.bank.account.AccountService;
import com.shinhan_hackathon.the_family_guardian.bank.util.BankUtil;
import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@Slf4j
@RequiredArgsConstructor
public class BankUserTest {
    private MockMvc mockMvc;
    private RestTemplate restTemplate = new RestTemplate();
    private final AccountService accountService = new AccountService();

    //    private Header header;
    private String apiName; // API 이름
    private String transmissionDate; // API 요청 일자(YYYYMMDD), 현재 날짜
    private String transmissionTime; // API 전송 시각(HHMMSS), 현재 시간 +-5
    private String institutionCode;
    private String fintechAppNo;
    private String apiServiceCode; // API 서비스 코드, apiName 필드와 동일값
    private String institutionTransactionUniqueNo; // 기관 거래 고유번호
    private String apiKey; // 발급받은 API Key
    private String userKey; // 회원가입할 때 받은 User Key
    private Map<String, String> header;

    @BeforeEach
    void setup() {
        restTemplate = new RestTemplate();

        apiName = "inquireDemandDepositAccountList";
        transmissionDate = BankUtil.generateTransmissionDate();
        transmissionTime = BankUtil.generateTransmissionTime();
        institutionCode = "00100";
        fintechAppNo = "001";
        apiServiceCode = apiName;
        institutionTransactionUniqueNo = BankUtil.generateInstitutionTransactionUniqueCodeNo();
        apiKey = null;
        userKey = null;

        // 헤더 객체를 생성
        header = new HashMap<>();
        header.put("apiName", apiName);
        header.put("transmissionDate", transmissionDate);
        header.put("transmissionTime", transmissionTime);
        header.put("institutionCode", institutionCode);
        header.put("fintechAppNo", fintechAppNo);
        header.put("apiServiceCode", apiServiceCode);
        header.put("institutionTransactionUniqueNo", institutionTransactionUniqueNo);
        header.put("apiKey", apiKey);
        header.put("userKey", userKey);

    }

    @Test
    @DisplayName("헤더_테스트")
    void test() {
        log.info("{}", header);
        System.out.println(header);
    }

    @Test
    @DisplayName("계좌_생성")
    void createAccount() throws Exception {
        String url = "/";
//        mockMvc.perform(post(url))
//                .andExpect(status().isOk())
//                .andDo(print());

//        System.out.println(restTemplate.postForObject(url, header, String.class));
    }

    @Test
    @DisplayName("계좌_목록_조회")
    void inquireAccountList() throws Exception {
        accountService.inquireAccountList();
    }
}

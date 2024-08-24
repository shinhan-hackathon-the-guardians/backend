package com.shinhan_hackathon.the_family_guardian.bank.account;

import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountTransactionHistoryResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.CheckAuthCodeResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.OpenAccountAuthResponse;
import com.shinhan_hackathon.the_family_guardian.bank.service.AccountAuthService;
import com.shinhan_hackathon.the_family_guardian.bank.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class BankAccountAuthTest {
    @Autowired
    private AccountAuthService authService;

    @Autowired
    private AccountService accountService;

    @BeforeEach
    void setup() {

    }

    // TODO: 각 서비스 코드들은 입력값을 받아 응답을 반환하는 구조롤 변환될 것
    // TODO: 테스트 코드도 그에 맞게 수정될 예정
    // TODO: 실행 시 입력값이 하드코딩되어 동작하지 않는 테스트가 있을 수 있음
    @Test
    @DisplayName("15_1원송금_요청_검증_성공")
    void accountAuthSuccess() {
        String accountNo = "0889289214082284";
        OpenAccountAuthResponse openAccountAuthResponse = authService.openAccountAuth(accountNo);
        log.info("{}", openAccountAuthResponse);

        Long transactionUniqueNo = openAccountAuthResponse.rec().transactionUniqueNo();
        AccountTransactionHistoryResponse accountTransactionHistoryResponse = accountService.inquireTransactionHistory(accountNo, transactionUniqueNo);
        log.info("{}", accountTransactionHistoryResponse);

        String transactionSummary = accountTransactionHistoryResponse.getRec().getTransactionSummary();
        String authCode = transactionSummary.split(" ")[1];

        CheckAuthCodeResponse checkAuthCodeResponse = authService.checkAuthCode(accountNo, authCode);
        log.info("{}", checkAuthCodeResponse);
    }

    @Test
    @DisplayName("15_1원송금_요청_검증_실패")
    void accountAuthFail() {
        String accountNo = "0889289214082284";
        OpenAccountAuthResponse openAccountAuthResponse = authService.openAccountAuth(accountNo);
        log.info("{}", openAccountAuthResponse);

        Assertions.assertThatThrownBy(() ->
                authService.checkAuthCode(accountNo, "0000")
        );
    }
}

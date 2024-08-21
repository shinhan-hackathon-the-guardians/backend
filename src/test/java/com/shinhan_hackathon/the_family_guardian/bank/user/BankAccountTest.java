package com.shinhan_hackathon.the_family_guardian.bank.user;

import com.shinhan_hackathon.the_family_guardian.bank.account.AccountService;
import com.shinhan_hackathon.the_family_guardian.bank.util.BankUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class BankAccountTest {
    @Autowired
    private AccountService accountService;

    @BeforeEach
    void setup() {

    }

    @Test
    @DisplayName("헤더_테스트")
    void test() {
        log.info("{}", BankUtil.createHeader("apiName"));
    }

    // TODO: 각 서비스 코드들은 입력값을 받아 응답을 반환하는 구조롤 변환될 것
    // TODO: 테스트 코드도 그에 맞게 수정될 예정
    // TODO: 실행 시 입력값이 하드코딩되어 동작하지 않는 테스트가 있을 수 있음

    @Test
    @DisplayName("1_상품_등록")
    void createDemandDeposit() {
        accountService.createDemandDeposit();
    }

    @Test
    @DisplayName("2_상품_조회")
    void inquireDemandDeposit() {
        accountService.inquireDemandDepositList();
    }

    @Test
    @DisplayName("3_계좌_생성")
    void createAccount() throws Exception {
        accountService.createAccount();
    }

    @Test
    @DisplayName("4_계좌_목록_조회")
    void inquireAccountList() throws Exception {
        accountService.inquireAccountList();
    }

    @Test
    @DisplayName("5_계좌_단건_조회")
    void inquireAccount() {
        accountService.inquireAccount();
    }

    @Test
    @DisplayName("6_예금주_조회")
    void inquireAccountHolderName() {
        accountService.inquireAccountHolderName();
    }

    @Test
    @DisplayName("7_계좌_잔액_조회")
    void inquireAccountBalance() {
        accountService.inquireAccountBalance();
    }

    @Test
    @DisplayName("8_계좌_츨금")
    void updateAccountWithdrawal() {
        accountService.updateAccountWithdrawal();
    }

    @Test
    @DisplayName("9_계좌_입금")
    void updateAccountDeposit() {
        accountService.updateAccountDeposit();
    }

    @Test
    @DisplayName("10_계좌_이체")
    void updateAccountTransfer() {
        accountService.updateAccountTransfer();
    }

    @Test
    @DisplayName("11_계좌_이체_한도_변경")
    void updateTransferLimit() {
        accountService.updateTransferLimit();
    }

    @Test
    @DisplayName("12_계좌_거래_내역_조회")
    void inquireAccountTransactionHistoryList() {
        accountService.inquireTransactionHistoryList();
    }

    @Test
    @DisplayName("13_계좌_거래_내역_단건_조회")
    void inquireAccountTransactionHistory() {
        accountService.inquireTransactionHistory();
    }

    @Test
    @DisplayName("14_계좌_해지")
    void deleteAccount() {
        accountService.deleteAccount();
    }
}

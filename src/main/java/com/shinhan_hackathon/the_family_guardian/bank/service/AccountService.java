package com.shinhan_hackathon.the_family_guardian.bank.service;

import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountBalanceRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountCreationRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountDeletionRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountDepositRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountHolderNameRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountListRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountTransactionHistoryListRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountTransactionHistoryRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountTransferLimitRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountTransferRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountWithdrawalRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.DemandDepositCreationRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.DemandDepositListRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountBalanceResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountCreationResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountDeletionResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountDepositResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountHolderNameResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountListResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountTransactionHistoryListResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountTransactionHistoryResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountTransferLimitResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountTransferResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountWithdrawalResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.DemandDepositCreationResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.DemandDepositListResponse;
import com.shinhan_hackathon.the_family_guardian.bank.util.BankUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private static final String BASE_URL = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit/";
    private final RestClient restClient = RestClient.create();
    private final BankUtil bankUtil;

    // TODO: 반환값 DTO 형태로 반환할 것 (완료)
    // TODO: 필요한 입력값을 받아 반환하는 코드로 변경할 것 (일부 필요한 기능들만 완료, 나머지는 기본 값 사용)
    // TODO: Test 코드에서 올바른 입력값으로 호출 (일부 필요한 기능들만 완료)

    // TODO: 2.4.1 상품 등록 : 은행별 수시입출금 상품을 등록
    public DemandDepositCreationResponse createDemandDeposit() {
        String apiName = "createDemandDeposit";
        DemandDepositCreationRequest demandDepositCreationRequest = DemandDepositCreationRequest.builder()
                .header(bankUtil.createHeader(apiName))
                .bankCode("088")
                .accountName("신한은행 수시입출금")
                .accountDescription("신한은행 수시입출금 상품입니다.")
                .build();

        return executePost("createDemandDeposit",
                demandDepositCreationRequest, DemandDepositCreationResponse.class).getBody();
    }

    // TODO: 2.4.2 상품 조회 : 은행별 계좌 상품 조회
    public DemandDepositListResponse inquireDemandDepositList() {
        String apiName = "inquireDemandDepositList";
        DemandDepositListRequest demandDepositListRequest = DemandDepositListRequest.builder()
                .header(bankUtil.createHeader(apiName))
                .build();

        return executePost("inquireDemandDepositList",
                demandDepositListRequest, DemandDepositListResponse.class).getBody();
    }

    // TODO: 2.4.3 사용자 계좌 생성 : 상품 고유번호를 통해 사용자의 계좌를 생성할 수 있음
    public AccountCreationResponse createAccount() {
        String apiName = "createDemandDepositAccount";
        String accountTypeUniqueNo = "088-1-3e9cb2bb399f4f"; // 하나의 상품만 사용하는 것으로 제한하겠음
        AccountCreationRequest accountCreationRequest = AccountCreationRequest.builder()
                .header(bankUtil.createHeader(apiName))
                .accountTypeUniqueNo(accountTypeUniqueNo)
                .build();

        return executePost("createDemandDepositAccount", accountCreationRequest,
                AccountCreationResponse.class).getBody();
    }

    // TODO: 2.4.4 사용자 계좌 목록 조회 : 사용자의 계촤 목록 전체를 조회
    public AccountListResponse inquireAccountList() {
        String apiName = "inquireDemandDepositAccountList";
        AccountListRequest accountListRequest = AccountListRequest.builder()
                .header(bankUtil.createHeader(apiName))
                .build();

        return executePost("inquireDemandDepositAccountList", accountListRequest,
                AccountListResponse.class).getBody();
    }

    // TODO: 2.4.5 계좌 조회 단건 : 특정 계좌에 대한 정보를 조회
    public AccountResponse inquireAccount(String accountNo) {
        String apiName = "inquireDemandDepositAccount";
        if (!StringUtils.hasText(accountNo)) {
            accountNo = "0885135436359049";
        }
        AccountRequest accountRequest = AccountRequest.builder()
                .header(bankUtil.createHeader(apiName))
                .accountNo(accountNo)
                .build();

        return executePost("inquireDemandDepositAccount", accountRequest, AccountResponse.class).getBody();
    }

    // TODO: 2.4.6 예금주 조회 : 계좌에 대한 예금주명 조회
    public AccountHolderNameResponse inquireAccountHolderName(String accountNo) {
        String apiName = "inquireDemandDepositAccountHolderName";
        if (!StringUtils.hasText(accountNo)) {
            accountNo = "0885135436359049";
        }
        AccountHolderNameRequest accountHolderNameRequest = AccountHolderNameRequest.builder()
                .header(bankUtil.createHeader(apiName))
                .accountNo(accountNo)
                .build();

       return executePost("inquireDemandDepositAccountHolderName",
                accountHolderNameRequest, AccountHolderNameResponse.class).getBody();
    }

    // TODO: 2.4.7 계좌 잔액 조회
    public AccountBalanceResponse inquireAccountBalance(String accountNo) {
        String apiName = "inquireDemandDepositAccountBalance";
//        String accountNo = "0885135436359049";
        AccountBalanceRequest accountRequest = AccountBalanceRequest.builder()
                .header(bankUtil.createHeader(apiName))
                .accountNo(accountNo)
                .build();

        return executePost("inquireDemandDepositAccountBalance", accountRequest,
                AccountBalanceResponse.class).getBody();
    }

    // TODO: 2.4.8 계좌 출금 : 사용자의 계좌로부터 대금을 출금
    public AccountWithdrawalResponse updateAccountWithdrawal(String accountNo, Long transactionBalance) {
        String apiName = "updateDemandDepositAccountWithdrawal";
        AccountWithdrawalRequest accountWithdrawalRequest = AccountWithdrawalRequest.builder()
                .header(bankUtil.createHeader(apiName))
                .accountNo(accountNo)
                .transactionBalance(transactionBalance)
                .transactionSummary("(수시입츨금) : 출금")
                .build();

        return executePost("updateDemandDepositAccountWithdrawal", accountWithdrawalRequest,
                AccountWithdrawalResponse.class).getBody();
    }

    // TODO: 2.4.9 계좌 입금 : 사용자의 계좌로부터 대금을 입금
    public AccountDepositResponse updateAccountDeposit(String accountNo, Long transactionBalance) {
        String apiName = "updateDemandDepositAccountDeposit";
        AccountDepositRequest accountDepositRequest = AccountDepositRequest.builder()
                .header(bankUtil.createHeader(apiName))
                .accountNo(accountNo)
                .transactionBalance(transactionBalance)
                .transactionSummary("(수시입츨금) : 출금")
                .build();

        return executePost("updateDemandDepositAccountDeposit", accountDepositRequest,
                AccountDepositResponse.class).getBody();
    }

    // TODO: 2.4.10 계좌 이체 : 한 계좌로부터 다른 계좌로 대금을 이체
    public AccountTransferResponse updateAccountTransfer(String depositAccountNo, String withdrawalAccountNo, Long transactionBalance) {
        String apiName = "updateDemandDepositAccountTransfer";
        AccountTransferRequest accountTransferRequest = AccountTransferRequest.builder()
                .header(bankUtil.createHeader(apiName))
                .depositAccountNo(depositAccountNo)
                .transactionBalance(transactionBalance)
                .withdrawalAccountNo(withdrawalAccountNo)
                .withdrawalTransactionSummary("(수시입츨금) : 입금(이체)")
                .build();

        return executePost("updateDemandDepositAccountTransfer", accountTransferRequest,
                AccountTransferResponse.class).getBody();
    }

    // TODO: 2.4.11 계좌 이체 한도 변경
    public AccountTransferLimitResponse updateTransferLimit() {
        String apiName = "updateTransferLimit";
        String accountNo = "0885135436359049";
        AccountTransferLimitRequest accountTransferLimitRequest = AccountTransferLimitRequest.builder()
                .header(bankUtil.createHeader(apiName))
                .accountNo(accountNo)
                .oneTimeTransferLimit(1000000000L) // 본 서비스가 한도를 대신 처리할 것임
                .dailyTransferLimit(1000000000L)
                .build();

        return executePost("updateTransferLimit", accountTransferLimitRequest, AccountTransferLimitResponse.class).getBody();
    }

    // TODO: 2.4.12 계좌 거래 내역 조회
    public AccountTransactionHistoryListResponse inquireTransactionHistoryList(String accountNo) {
        String apiName = "inquireTransactionHistoryList";
        AccountTransactionHistoryListRequest accountTransactionHistoryListRequest = AccountTransactionHistoryListRequest.builder()
                .header(bankUtil.createHeader(apiName))
                .accountNo(accountNo)
                .startDate("20240801")
                .endDate("20241231")
                .transactionType("A")
                .orderByType("ASC")
                .build();

        return executePost("inquireTransactionHistoryList",
                accountTransactionHistoryListRequest, AccountTransactionHistoryListResponse.class).getBody();
    }

    // TODO: 2.4.13 계좌 거래 내역 조회 (단건)
    public AccountTransactionHistoryResponse inquireTransactionHistory(String accountNo, Long transactionUniqueNo) {
        String apiName = "inquireTransactionHistory";
        AccountTransactionHistoryRequest accountTransactionHistoryRequest = AccountTransactionHistoryRequest.builder()
                .header(bankUtil.createHeader(apiName))
                .accountNo(accountNo)
                .transactionUniqueNo(transactionUniqueNo)
                .build();

        return executePost("inquireTransactionHistory", accountTransactionHistoryRequest,
                AccountTransactionHistoryResponse.class).getBody();
    }

    // TODO: 2.4.14 계좌 해지
    public AccountDeletionResponse deleteAccount(String accountNo, String refundAccountNo) {
        String apiName = "deleteDemandDepositAccount";
        AccountDeletionRequest accountDeletionRequest = AccountDeletionRequest.builder()
                .header(bankUtil.createHeader(apiName))
                .accountNo(accountNo)
                .refundAccountNo(refundAccountNo)
                .build();

        return executePost("deleteDemandDepositAccount", accountDeletionRequest,
                AccountDeletionResponse.class).getBody();
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

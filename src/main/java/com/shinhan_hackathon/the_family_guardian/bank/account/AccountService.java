package com.shinhan_hackathon.the_family_guardian.bank.account;

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
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private static final String BASE_URL = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit/";
    private final RestClient restClient = RestClient.create();

    // TODO: 반환값 DTO 형태로 반환할 것, 필요할지 Test 코드로 단순 실행할지는 추가적 고민이 필요함

    // TODO: 2.4.1 상품 등록 : 은행별 수시입출금 상품을 등록
    public DemandDepositCreationResponse createDemandDeposit() {
        String apiName = "createDemandDeposit";
        DemandDepositCreationRequest demandDepositCreationRequest = DemandDepositCreationRequest.builder()
                .header(BankUtil.createHeader(apiName))
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
                .header(BankUtil.createHeader(apiName))
                .build();

        return executePost("inquireDemandDepositList",
                demandDepositListRequest, DemandDepositListResponse.class).getBody();
    }

    // TODO: 2.4.3 사용자 계좌 생성 : 상품 고유번호를 통해 사용자의 계좌를 생성할 수 있음
    public AccountCreationResponse createAccount() {
        String apiName = "createDemandDepositAccount";
        String accountTypeUniqueNo = "088-1-3e9cb2bb399f4f";
        AccountCreationRequest accountCreationRequest = AccountCreationRequest.builder()
                .header(BankUtil.createHeader(apiName))
                .accountTypeUniqueNo(accountTypeUniqueNo)
                .build();

        return executePost("createDemandDepositAccount", accountCreationRequest,
                AccountCreationResponse.class).getBody();
    }

    // TODO: 2.4.4 사용자 계좌 목록 조회 : 사용자의 계촤 목록 전체를 조회
    public AccountListResponse inquireAccountList() {

        String apiName = "inquireDemandDepositAccountList";
        AccountListRequest accountListRequest = AccountListRequest.builder()
                .header(BankUtil.createHeader(apiName))
                .build();

        return executePost("inquireDemandDepositAccountList", accountListRequest,
                AccountListResponse.class).getBody();
    }

    // TODO: 2.4.5 계좌 조회 단건 : 특정 계좌에 대한 정보를 조회
    public AccountResponse inquireAccount() {

        String apiName = "inquireDemandDepositAccount";
        String accountNo = "0885135436359049";
        AccountRequest accountRequest = AccountRequest.builder()
                .header(BankUtil.createHeader(apiName))
                .accountNo(accountNo)
                .build();

        return executePost("inquireDemandDepositAccount", accountRequest, AccountResponse.class).getBody();
    }

    // TODO: 2.4.6 예금주 조회 : 계좌에 대한 예금주명 조회
    public AccountHolderNameResponse inquireAccountHolderName() {
        String apiName = "inquireDemandDepositAccountHolderName";
        String accountNo = "0885135436359049";
        AccountHolderNameRequest accountHolderNameRequest = AccountHolderNameRequest.builder()
                .header(BankUtil.createHeader(apiName))
                .accountNo(accountNo)
                .build();

       return executePost("inquireDemandDepositAccountHolderName",
                accountHolderNameRequest, AccountHolderNameResponse.class).getBody();
    }

    // TODO: 2.4.7 계좌 잔액 조회
    public AccountBalanceResponse inquireAccountBalance() {
        String apiName = "inquireDemandDepositAccountBalance";
        String accountNo = "0885135436359049";
        AccountBalanceRequest accountRequest = AccountBalanceRequest.builder()
                .header(BankUtil.createHeader(apiName))
                .accountNo(accountNo)
                .build();

        return executePost("inquireDemandDepositAccountBalance", accountRequest,
                AccountBalanceResponse.class).getBody();
    }

    // TODO: 2.4.8 계좌 출금 : 사용자의 계좌로부터 대금을 출금
    public AccountWithdrawalResponse updateAccountWithdrawal() {
        String apiName = "updateDemandDepositAccountWithdrawal";
        String accountNo = "0885135436359049";
        AccountWithdrawalRequest accountWithdrawalRequest = AccountWithdrawalRequest.builder()
                .header(BankUtil.createHeader(apiName))
                .accountNo(accountNo)
                .transactionBalance(100000L)
                .transactionSummary("(수시입츨금) : 출금")
                .build();

        return executePost("updateDemandDepositAccountWithdrawal", accountWithdrawalRequest,
                AccountWithdrawalResponse.class).getBody();
    }

    // TODO: 2.4.9 계좌 입금 : 사용자의 계좌로부터 대금을 입금
    public AccountDepositResponse updateAccountDeposit() {
        String apiName = "updateDemandDepositAccountDeposit";
        String accountNo = "0885135436359049";
        AccountDepositRequest accountDepositRequest = AccountDepositRequest.builder()
                .header(BankUtil.createHeader(apiName))
                .accountNo(accountNo)
                .transactionBalance(100000L)
                .transactionSummary("(수시입츨금) : 출금")
                .build();

        return executePost("updateDemandDepositAccountDeposit", accountDepositRequest,
                AccountDepositResponse.class).getBody();
    }

    // TODO: 2.4.10 계좌 이체 : 한 게좌로부터 다른 계좌로 대금을 이체
    public AccountTransferResponse updateAccountTransfer() {
        String apiName = "updateDemandDepositAccountTransfer";
        String depositAccountNo = "0884755843206405";
        String withdrawalAccountNo = "0885135436359049";
        AccountTransferRequest accountTransferRequest = AccountTransferRequest.builder()
                .header(BankUtil.createHeader(apiName))
                .depositAccountNo(depositAccountNo)
                .transactionBalance(100000L)
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
                .header(BankUtil.createHeader(apiName))
                .accountNo(accountNo)
                .oneTimeTransferLimit(1000000L)
                .dailyTransferLimit(10000000L)
                .build();

        return executePost("updateTransferLimit", accountTransferLimitRequest, AccountTransferLimitResponse.class).getBody();
    }

    // TODO: 2.4.12 계좌 거래 내역 조회
    public AccountTransactionHistoryListResponse inquireTransactionHistoryList() {
        String apiName = "inquireTransactionHistoryList";
        String accountNo = "0885135436359049";
        AccountTransactionHistoryListRequest accountTransactionHistoryListRequest = AccountTransactionHistoryListRequest.builder()
                .header(BankUtil.createHeader(apiName))
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
    public AccountTransactionHistoryResponse inquireTransactionHistory() {
        String apiName = "inquireTransactionHistory";
        String accountNo = "0885135436359049";
        AccountTransactionHistoryRequest accountTransactionHistoryRequest = AccountTransactionHistoryRequest.builder()
                .header(BankUtil.createHeader(apiName))
                .accountNo(accountNo)
                .transactionUniqueNo(2210L)
                .build();

        return executePost("inquireTransactionHistory", accountTransactionHistoryRequest,
                AccountTransactionHistoryResponse.class).getBody();
    }

    // TODO: 2.4.14 계좌 해지
    public AccountDeletionResponse deleteAccount() {
        String apiName = "deleteDemandDepositAccount";
        String accountNo = "0881201572623750";
        String refundAccountNo = "0885135436359049";
        AccountDeletionRequest accountDeletionRequest = AccountDeletionRequest.builder()
                .header(BankUtil.createHeader(apiName))
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

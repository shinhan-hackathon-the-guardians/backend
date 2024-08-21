package com.shinhan_hackathon.the_family_guardian.bank.account;

import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountBalanceRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountCreationRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountDeletionRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountDepositRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountHolderNameRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountTransactionHistoryListRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountTransactionHistoryRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountTransferLimitRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountTransferRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountWithdrawalRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.DemandDepositCreationRequest;
import com.shinhan_hackathon.the_family_guardian.bank.dto.request.DemandDepositListRequest;
import com.shinhan_hackathon.the_family_guardian.bank.util.BankUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.shinhan_hackathon.the_family_guardian.bank.dto.request.AccountListRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	private static final String BASE_URL = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit/";
	private final RestClient restClient = RestClient.create();


	// TODO: 반환값 DTO 형태로 반환할 것, 필요할지 Test 코드로 단순 실행할지는 추가적 고민이 필요함

	// TODO: 2.4.1 상품 등록 : 은행별 수시입출금 상품을 등록
	public void createDemandDeposit() {
		String apiName = "createDemandDeposit";
		DemandDepositCreationRequest demandDepositCreationRequest = DemandDepositCreationRequest.builder()
				.header(BankUtil.createHeader(apiName))
				.bankCode("088")
				.accountName("신한은행 수시입출금")
				.accountDescription("신한은행 수시입출금 상품입니다.")
				.build();

		ResponseEntity<String> response = executePost("createDemandDeposit", demandDepositCreationRequest);
		System.out.println(response);
	}

	// TODO: 2.4.2 상품 조회 : 은행별 계좌 상품 조회
	public void inquireDemandDepositList() {
		String apiName = "inquireDemandDepositList";
		DemandDepositListRequest demandDepositListRequest = DemandDepositListRequest.builder()
				.header(BankUtil.createHeader(apiName))
				.build();

		ResponseEntity<String> response = executePost("inquireDemandDepositList", demandDepositListRequest);
		System.out.println(response);
	}

	// TODO: 2.4.3 사용자 계좌 생성 : 상품 고유번호를 통해 사용자의 계좌를 생성할 수 있음
	public void createAccount() {
		String apiName = "createDemandDepositAccount";
		String accountTypeUniqueNo = "088-1-3e9cb2bb399f4f";
		AccountCreationRequest accountCreationRequest = AccountCreationRequest.builder()
				.header(BankUtil.createHeader(apiName))
				.accountTypeUniqueNo(accountTypeUniqueNo)
				.build();

		ResponseEntity<String> response = executePost("createDemandDepositAccount", accountCreationRequest);
		System.out.println(response);
	}

	// TODO: 2.4.4 사용자 계좌 목록 조회 : 사용자의 계촤 목록 전체를 조회
	public void inquireAccountList() {

		String apiName = "inquireDemandDepositAccountList";
		AccountListRequest accountListRequest = AccountListRequest.builder()
			.header(BankUtil.createHeader(apiName))
			.build();

		ResponseEntity<String> response = executePost("inquireDemandDepositAccountList", accountListRequest);
		System.out.println(response);
	}

	// TODO: 2.4.5 계좌 조회 단건 : 특정 계좌에 대한 정보를 조회
	public void inquireAccount() {

		String apiName = "inquireDemandDepositAccount";
		String accountNo = "0885135436359049";
		AccountRequest accountRequest = AccountRequest.builder()
				.header(BankUtil.createHeader(apiName))
				.accountNo(accountNo)
				.build();

		ResponseEntity<String> response = executePost("inquireDemandDepositAccount", accountRequest);
		System.out.println(response);
	}

	// TODO: 2.4.6 예금주 조회 : 계좌에 대한 예금주명 조회
	public void inquireAccountHolderName() {
		String apiName = "inquireDemandDepositAccountHolderName";
		String accountNo = "0885135436359049";
		AccountHolderNameRequest accountHolderNameRequest = AccountHolderNameRequest.builder()
				.header(BankUtil.createHeader(apiName))
				.accountNo(accountNo)
				.build();

		ResponseEntity<String> response = executePost("inquireDemandDepositAccountHolderName", accountHolderNameRequest);
		System.out.println(response);
	}

	// TODO: 2.4.7 계좌 잔액 조회
	public void inquireAccountBalance() {
		String apiName = "inquireDemandDepositAccountBalance";
		String accountNo = "0885135436359049";
		AccountBalanceRequest accountRequest = AccountBalanceRequest.builder()
				.header(BankUtil.createHeader(apiName))
				.accountNo(accountNo)
				.build();

		ResponseEntity<String> response = executePost("inquireDemandDepositAccountBalance", accountRequest);
		System.out.println(response);
	}

	// TODO: 2.4.8 계좌 출금 : 사용자의 계좌로부터 대금을 출금
	public void  updateAccountWithdrawal() {
		String apiName = "updateDemandDepositAccountWithdrawal";
		String accountNo = "0885135436359049";
		AccountWithdrawalRequest accountWithdrawalRequest = AccountWithdrawalRequest.builder()
				.header(BankUtil.createHeader(apiName))
				.accountNo(accountNo)
				.transactionBalance(100000L)
				.transactionSummary("(수시입츨금) : 출금")
				.build();

		ResponseEntity<String> response = executePost("updateDemandDepositAccountWithdrawal", accountWithdrawalRequest);
		System.out.println(response);
	}

	// TODO: 2.4.9 계좌 입금 : 사용자의 계좌로부터 대금을 입금
	public void  updateAccountDeposit() {
		String apiName = "updateDemandDepositAccountDeposit";
		String accountNo = "0885135436359049";
		AccountDepositRequest accountDepositRequest = AccountDepositRequest.builder()
				.header(BankUtil.createHeader(apiName))
				.accountNo(accountNo)
				.transactionBalance(100000L)
				.transactionSummary("(수시입츨금) : 출금")
				.build();

		ResponseEntity<String> response = executePost("updateDemandDepositAccountDeposit", accountDepositRequest);
		System.out.println(response);
	}

	// TODO: 2.4.10 계좌 이체 : 한 게좌로부터 다른 계좌로 대금을 이체
	public void updateAccountTransfer() {
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

		ResponseEntity<String> response = executePost("updateDemandDepositAccountTransfer", accountTransferRequest);
		System.out.println(response);
	}

	// TODO: 2.4.11 계좌 이체 한도 변경
	public void updateTransferLimit() {
		String apiName = "updateTransferLimit";
		String accountNo = "0885135436359049";
		AccountTransferLimitRequest accountTransferLimitRequest = AccountTransferLimitRequest.builder()
				.header(BankUtil.createHeader(apiName))
				.accountNo(accountNo)
				.oneTimeTransferLimit(1000000L)
				.dailyTransferLimit(10000000L)
				.build();

		ResponseEntity<String> response = executePost("updateTransferLimit", accountTransferLimitRequest);
		System.out.println(response);
	}

	// TODO: 2.4.12 계좌 거래 내역 조회
	public void inquireTransactionHistoryList() {
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

		ResponseEntity<String> response = executePost("inquireTransactionHistoryList", accountTransactionHistoryListRequest);
		System.out.println(response);
	}

	// TODO: 2.4.13 계좌 거래 내역 조회 (단건)
	public void inquireTransactionHistory() {
		String apiName = "inquireTransactionHistory";
		String accountNo = "0885135436359049";
		AccountTransactionHistoryRequest accountTransactionHistoryRequest = AccountTransactionHistoryRequest.builder()
				.header(BankUtil.createHeader(apiName))
				.accountNo(accountNo)
				.transactionUniqueNo(2210L)
				.build();

		ResponseEntity<String> response = executePost("inquireTransactionHistory", accountTransactionHistoryRequest);
		System.out.println(response);
	}

	// TODO: 2.4.14 계좌 해지
	public void deleteAccount() {
		String apiName = "deleteDemandDepositAccount";
		String accountNo = "0012432983760475";
		String refundAccountNo = "0885135436359049";
		AccountDeletionRequest accountDeletionRequest = AccountDeletionRequest.builder()
				.header(BankUtil.createHeader(apiName))
				.accountNo(accountNo)
				.refundAccountNo(refundAccountNo)
				.build();

		ResponseEntity<String> response = executePost("deleteDemandDepositAccount", accountDeletionRequest);
		System.out.println(response);
	}

	private ResponseEntity<String> executePost(String endpoint, Object requestBody) {
		return restClient.post()
				.uri(BASE_URL + endpoint)
				.contentType(MediaType.APPLICATION_JSON)
				.body(requestBody)
				.retrieve()
				.toEntity(String.class);
	}
}

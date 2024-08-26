package com.shinhan_hackathon.the_family_guardian.domain.user.service;

import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountTransactionHistoryResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.CheckAuthCodeResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.OpenAccountAuthResponse;
import com.shinhan_hackathon.the_family_guardian.bank.service.AccountAuthService;
import com.shinhan_hackathon.the_family_guardian.bank.service.AccountService;
import com.shinhan_hackathon.the_family_guardian.bank.util.BankUtil;
import com.shinhan_hackathon.the_family_guardian.domain.user.dto.AccountAuthResponse;
import com.shinhan_hackathon.the_family_guardian.domain.user.dto.SignupRequest;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import com.shinhan_hackathon.the_family_guardian.domain.user.repository.UserRepository;
import com.shinhan_hackathon.the_family_guardian.global.service.RedisService;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final AccountAuthService accountAuthService;
    private final AccountService accountService;
    private final RedisService redisService;

    @Transactional
    public void createUser(SignupRequest signupRequest) {

        validateSignupRequest(signupRequest);
        validateCsrfToken(signupRequest.accountNumber(), signupRequest.csrfToken());

        userRepository.save(signupRequest.toUserEntity());
        redisService.deleteValues(signupRequest.accountNumber());
    }

    public AccountAuthResponse openAccountAuth(String accountNo) {
        OpenAccountAuthResponse openAccountAuthResponse = accountAuthService.openAccountAuth(accountNo);

        BankUtil.validateBankApiResponse(openAccountAuthResponse.header());

        String csrfToken = UUID.randomUUID().toString();
        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(5);
        redisService.setValues(accountNo, csrfToken, expireTime);

        log.info("accountNo:{} csrf:{} txNo:{}", accountNo, csrfToken,
                openAccountAuthResponse.rec().transactionUniqueNo());

        String accountAuthCode = getAccountAuthCode(accountNo, openAccountAuthResponse);
        log.info("[NOTIFICATION] account auth code: {}", accountAuthCode);

        return new AccountAuthResponse(openAccountAuthResponse.rec().accountNo(), csrfToken);
    }

    private String getAccountAuthCode(String accountNo, OpenAccountAuthResponse openAccountAuthResponse) {
        AccountTransactionHistoryResponse accountTransactionHistoryResponse = accountService.inquireTransactionHistory(
                accountNo, openAccountAuthResponse.rec().transactionUniqueNo());
        String transactionSummary = accountTransactionHistoryResponse.getRec().getTransactionSummary();
        return transactionSummary.split(" ")[1];
    }

    public AccountAuthResponse checkAccountAuth(String accountNo, String authCode, String csrfToken) {
        String storedCsrfToken = validateCsrfToken(accountNo, csrfToken);

        CheckAuthCodeResponse checkAuthCodeResponse = accountAuthService.checkAuthCode(accountNo, authCode);
        BankUtil.validateBankApiResponse(checkAuthCodeResponse.header());

        csrfToken = storedCsrfToken + UUID.randomUUID();
        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(5);
        redisService.setValues(accountNo, csrfToken, expireTime);

        log.info("1원송금 인증 성공");
        return new AccountAuthResponse(checkAuthCodeResponse.rec().accountNo(), csrfToken);
    }

    private String validateCsrfToken(String accountNumber, String csrfToken) {
        String storedCsrfToken = redisService.getValues(accountNumber)
                .orElseThrow(() -> new RuntimeException("1원 인증내역이 없습니다."));

        if (!storedCsrfToken.equals(csrfToken)) {
            redisService.deleteValues(accountNumber);
            throw new RuntimeException("csrf 토큰이 일치하지 않습니다.");
        }

        return storedCsrfToken;
    }

    public boolean validateSignupRequest(SignupRequest signupRequest) {
        return true;
    }

    public String validUsername(String username) {
        // TODO 검증로직 추가
        return username;
    }

    public String validPassword(String password, String passwordCheck) {
        if (password == null || passwordCheck == null) {
            throw new IllegalArgumentException("pw가 null입니다.");
        }

        if (!password.equals(passwordCheck)) {
            throw new IllegalArgumentException("pw가 일치하지 않습니다.");
        }

        return password;
    }

    public String getAccountNumber(Long userId) {
        log.info("UserService.getAccountNumber() is called.");
        User user = userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("Failed to found user."));

        return user.getAccountNumber();
    }


}

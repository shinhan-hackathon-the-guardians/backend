package com.shinhan_hackathon.the_family_guardian.domain.user.service;

import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountTransactionHistoryResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.CheckAuthCodeResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.OpenAccountAuthResponse;
import com.shinhan_hackathon.the_family_guardian.bank.service.AccountAuthService;
import com.shinhan_hackathon.the_family_guardian.bank.service.AccountService;
import com.shinhan_hackathon.the_family_guardian.bank.util.BankUtil;
import com.shinhan_hackathon.the_family_guardian.domain.payment.entity.LimitPeriod;
import com.shinhan_hackathon.the_family_guardian.domain.payment.entity.PaymentLimit;
import com.shinhan_hackathon.the_family_guardian.domain.payment.repository.PaymentLimitRepository;
import com.shinhan_hackathon.the_family_guardian.domain.payment.service.PaymentLimitService;
import com.shinhan_hackathon.the_family_guardian.domain.user.dto.AccountAuthResponse;
import com.shinhan_hackathon.the_family_guardian.domain.user.dto.SignupRequest;
import com.shinhan_hackathon.the_family_guardian.domain.user.dto.UpdateDeviceTokenResponse;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import com.shinhan_hackathon.the_family_guardian.domain.user.repository.UserRepository;
import com.shinhan_hackathon.the_family_guardian.global.auth.util.AuthUtil;
import com.shinhan_hackathon.the_family_guardian.global.redis.service.RedisService;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    public static final int SINGLE_TRANSACTION_LIMIT_DEFAULT = 1_000_000;
    public static final int MAX_LIMIT_AMOUNT_DEFAULT = 20_000_000;

    private final UserRepository userRepository;
    private final PaymentLimitService paymentLimitService;
    private final AccountAuthService accountAuthService;
    private final AccountService accountService;
    private final RedisService redisService;
    private final AuthUtil authUtil;
    private final PaymentLimitRepository paymentLimitRepository;

    @Transactional
    public void createUser(SignupRequest signupRequest) {

        validateSignupRequest(signupRequest);
        validateCsrfToken(signupRequest.accountNumber(), signupRequest.csrfToken());

        User user = userRepository.save(signupRequest.toUserEntity());
        redisService.deleteValues(signupRequest.accountNumber());

        PaymentLimit paymentLimit = new PaymentLimit(
                user,
                Timestamp.from(Instant.now()),
                LimitPeriod.DAY30,
                SINGLE_TRANSACTION_LIMIT_DEFAULT,
                MAX_LIMIT_AMOUNT_DEFAULT,
                0
        );
        paymentLimitRepository.save(paymentLimit);
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

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("Failed to found user."));
    }

    @Transactional
    public UpdateDeviceTokenResponse setDeviceToken(String deviceToken) {
        Long userId = Long.valueOf(authUtil.getUserPrincipal().getUsername());
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        String updatedDeviceToken = user.updateDeviceToken(deviceToken);

        authUtil.updateAuthentication(user);
        return new UpdateDeviceTokenResponse(updatedDeviceToken);
    }

    // TODO: 매일 00시에 실행되는 AmountUsed 초기화
    @Scheduled(cron = "0 0 0 * * ?") // 매일 00:00에 실행
    @Transactional
    public void updateAmountUsed() {
        log.info("UserService.updateAmountUsed() is called.");
        List<PaymentLimit> paymentLimitList = paymentLimitService.findAllPaymentLimit();
        paymentLimitList.stream().forEach(paymentLimit -> {
            User user = paymentLimit.getUser();

        });

        // TODO: PaymentLimitList 조회
        // TODO: 각 PaymentLimit의 User 조회
        // TODO: 각 User의 period 조회
        // TODO: 현재 날짜와 비교
        // TODO: 정해진 period를 넘었으면, 초기화
    }
}

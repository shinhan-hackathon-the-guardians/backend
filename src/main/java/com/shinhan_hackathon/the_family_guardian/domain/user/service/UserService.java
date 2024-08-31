package com.shinhan_hackathon.the_family_guardian.domain.user.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountTransactionHistoryResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.CheckAuthCodeResponse;
import com.shinhan_hackathon.the_family_guardian.bank.dto.response.OpenAccountAuthResponse;
import com.shinhan_hackathon.the_family_guardian.bank.service.AccountAuthService;
import com.shinhan_hackathon.the_family_guardian.bank.service.AccountService;
import com.shinhan_hackathon.the_family_guardian.bank.util.BankUtil;
import com.shinhan_hackathon.the_family_guardian.domain.approval.entity.AcceptStatus;
import com.shinhan_hackathon.the_family_guardian.domain.approval.entity.Approval;
import com.shinhan_hackathon.the_family_guardian.domain.approval.repository.ApprovalRepository;
import com.shinhan_hackathon.the_family_guardian.domain.approval.service.ApprovalService;
import com.shinhan_hackathon.the_family_guardian.domain.family.dto.FamilyInviteNotification;
import com.shinhan_hackathon.the_family_guardian.domain.family.entity.Family;
import com.shinhan_hackathon.the_family_guardian.domain.payment.entity.LimitPeriod;
import com.shinhan_hackathon.the_family_guardian.domain.payment.entity.PaymentLimit;
import com.shinhan_hackathon.the_family_guardian.domain.payment.repository.PaymentLimitRepository;
import com.shinhan_hackathon.the_family_guardian.domain.payment.service.PaymentLimitService;
import com.shinhan_hackathon.the_family_guardian.domain.user.dto.*;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Role;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import com.shinhan_hackathon.the_family_guardian.domain.user.repository.UserRepository;
import com.shinhan_hackathon.the_family_guardian.global.auth.dto.UserPrincipal;
import com.shinhan_hackathon.the_family_guardian.global.auth.util.AuthUtil;
import com.shinhan_hackathon.the_family_guardian.global.fcm.FcmSender;
import com.shinhan_hackathon.the_family_guardian.global.redis.service.RedisService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    private final ApprovalService approvalService;
    private final ApprovalRepository approvalRepository;
    private final FcmSender fcmSender;
	private final ObjectMapper jacksonObjectMapper;

	@Transactional
    public LoginResponse createUser(SignupRequest signupRequest) {

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

		Long familyId = null;
		String familyName = null;
		if (user.getFamily() != null) {
			familyId = user.getFamily().getId();
			familyName = user.getFamily().getName();
		}

		return new LoginResponse(
				user.getId(),
				user.getName(),
				user.getLevel(),
				user.getRole(),
				familyId,
				familyName
		);
	}

    public AccountAuthResponse openAccountAuth(String accountNo, String deviceToken) {
        OpenAccountAuthResponse openAccountAuthResponse = accountAuthService.openAccountAuth(accountNo);

        BankUtil.validateBankApiResponse(openAccountAuthResponse.header());

        String csrfToken = UUID.randomUUID().toString();
        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(5);
        redisService.setValues(accountNo, csrfToken, expireTime);

        log.info("accountNo:{} csrf:{} txNo:{}", accountNo, csrfToken,
                openAccountAuthResponse.rec().transactionUniqueNo());

        String accountAuthCode = getAccountAuthCode(accountNo, openAccountAuthResponse);
        log.info("[NOTIFICATION] account auth code: {}", accountAuthCode);
		HashMap<String, String> map = new HashMap<>();
		map.put("auth_code", accountAuthCode);
		map.put("bank", "신한");
		map.put("account_no", accountNo);
		map.put("transaction_balance", "1");
        try {
			String bodyStr = jacksonObjectMapper.writeValueAsString(map);
			fcmSender.sendMessage(deviceToken, "인증", bodyStr);

			return new AccountAuthResponse(openAccountAuthResponse.rec().accountNo(), csrfToken);
		} catch (JsonProcessingException e) {
			log.info("1원송금 fcm body 변환 실패");
			throw new RuntimeException(e);
		}
    }

    private String getAccountAuthCode(String accountNo, OpenAccountAuthResponse openAccountAuthResponse) {
        AccountTransactionHistoryResponse accountTransactionHistoryResponse = accountService.inquireTransactionHistory(
                accountNo, openAccountAuthResponse.rec().transactionUniqueNo());
        String transactionSummary = accountTransactionHistoryResponse.getRec().getTransactionSummary();
        return transactionSummary;
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
		LocalDate today = LocalDate.now(ZoneId.systemDefault());
		paymentLimitList.forEach(paymentLimit -> {
			LocalDate startDate = LocalDate.ofInstant(paymentLimit.getStartDate().toInstant(), ZoneId.systemDefault());
			LimitPeriod period = paymentLimit.getPeriod();
			LocalDate resetDate = calculateResetDate(startDate, period); // 초기화 날짜 계산

			if (resetDate.isEqual(today) || resetDate.isBefore(today)) { // 초기화 날이 오늘이거나, 지났으면
				paymentLimit.initializeAmountUsed(); // 현재 사용량을 0으로 초기화
				paymentLimit.initializeStartDate();  // StartDate 오늘로 최신화
				log.info("Amount used has been reset for user: " + paymentLimit.getUser().getId());
			}
		});
	}

	private LocalDate calculateResetDate(LocalDate startDate, LimitPeriod period) {
		switch (period) {
			case DAY1 -> {
				return startDate.plusDays(1);
			}
			case DAY7 -> {
				return startDate.plusDays(7);
			}
			case DAY15 -> {
				return startDate.plusDays(15);
			}
			case DAY30 -> {
				return startDate.plusMonths(1);
			}
			default -> throw new IllegalArgumentException("Unknown period: " + period);
		}
	}

	public Long getUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		return userPrincipal.user().getId();
	}

	public UserInfoResponse getUserInfo() {
		log.info("UserService.getUserInfo() is called.");
		User user = getUser(getUserId());

		Long familyId = null;
		String familyName = null;
		Family family = user.getFamily();
		if (family != null) {
			familyId = family.getId();
			familyName = family.getName();
		}

		return UserInfoResponse.builder()
			.name(user.getName())
			.level(user.getLevel())
			.role(user.getRole())
			.familyId(familyId)
			.familyName(familyName)
			.build();
	}

	public User findByAccountNumber(String accountNumber) {
		return userRepository.findByAccountNumber(accountNumber)
			.orElseThrow(() -> new EntityNotFoundException("Failed to found user."));
	}

	// TODO: PaymentLimitList 조회
	// TODO: 각 PaymentLimit의 User 조회
	// TODO: 각 User의 period 조회
	// TODO: 현재 날짜와 비교 starDate + period <= today
	// TODO: 정해진 period를 넘었으면, 초기화
    public List<FamilyInviteNotification> findFamilyInviteRequest() {
//        Long userId = Long.valueOf(authUtil.getUserPrincipal().getUsername());

        User user = userRepository.getReferenceById(1L);
        List<Approval> approvalList = approvalRepository.findAllByUser(user);

        return approvalList.stream()
                .filter(approval -> approval.getAccepted().equals(AcceptStatus.PROGRESS))
                .map(approval -> {
            Family family = approval.getFamily();
            User familyOwner = userRepository.findByFamilyAndRole(family, Role.OWNER).orElseThrow(() -> new RuntimeException("가족이 없습니다."));
            return new FamilyInviteNotification(
                    family.getId(),
                    family.getName(),
                    family.getDescription(),
                    familyOwner.getId(),
                    familyOwner.getName()
            );
        }).toList();
    }

	public Boolean checkUsernameExists(String username) {
		return userRepository.existsByUsername(username);
	}

	// TODO: PaymentLimitList 조회
    // TODO: 각 PaymentLimit의 User 조회
    // TODO: 각 User의 period 조회
    // TODO: 현재 날짜와 비교 starDate + period <= today
    // TODO: 정해진 period를 넘었으면, 초기화
}

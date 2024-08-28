package com.shinhan_hackathon.the_family_guardian.domain.payment.entity;

import java.sql.Timestamp;

import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "PaymentLimit")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class PaymentLimit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false)
	private Timestamp startDate;

	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private LimitPeriod period;

	private Integer singleTransactionLimit; // 단건 결제 가능액
	private Integer maxLimitAmount; // 기간 동안 최대 결제 누적 한도
	private Integer amountUsed; // 현재 결제 사용량

	@Builder
	public PaymentLimit(User user, Timestamp startDate, LimitPeriod period, Integer singleTransactionLimit,
						Integer maxLimitAmount, Integer amountUsed) {
		this.user = user;
		this.startDate = startDate;
		this.period = period;
		this.singleTransactionLimit = singleTransactionLimit;
		this.maxLimitAmount = maxLimitAmount;
		this.amountUsed = amountUsed;
	}

	public LimitPeriod updatePeriod(LimitPeriod period) {
		return this.period = period;
	}

	public Integer updateSingleTransactionLimit(Integer singleTransactionLimit) {
		return this.singleTransactionLimit = singleTransactionLimit;
	}

	public Integer updateMaxLimitAmount(Integer maxLimitAmount) {
		return this.maxLimitAmount = maxLimitAmount;
	}


}

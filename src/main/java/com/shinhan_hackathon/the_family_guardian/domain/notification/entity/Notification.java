package com.shinhan_hackathon.the_family_guardian.domain.notification.entity;

import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.TransactionRequest;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "Notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false) // Transaction 주인, 조회 용이성을 위해 사용
	private User user;

	@OneToOne
	@JoinColumn(name = "transaction_id", nullable = false)
	private TransactionRequest transaction;

	@Column(nullable = false, length = 50)
	private String source; // 알림 종류 : 출금 제한, 결제 한도

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String body;

	@Column(nullable = false, columnDefinition = "boolean default false")
	private boolean requiresResponse; // 응답이 필요한 알림인지

	@Builder
	public Notification(User user, TransactionRequest transaction, String source, String title, String body,
						boolean requiresResponse) {
		this.user = user;
		this.transaction = transaction;
		this.source = source;
		this.title = title;
		this.body = body;
		this.requiresResponse = requiresResponse;
	}
}

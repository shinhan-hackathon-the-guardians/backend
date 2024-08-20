package com.shinhan_hackathon.the_family_guardian.domain.guardian.entity;

import java.sql.Timestamp;

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
@Table(name = "GuardianTest")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class GuardianTest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false)
	private int highScore;

	@Column(nullable = false)
	private boolean result;

	@Column(nullable = false)
	private Timestamp registrationDate;

	@Builder
	public GuardianTest(User user, int highScore, boolean result, Timestamp registrationDate) {
		this.user = user;
		this.highScore = highScore;
		this.result = result;
		this.registrationDate = registrationDate;
	}
}

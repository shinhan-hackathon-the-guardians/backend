package com.shinhan_hackathon.the_family_guardian.domain.user.entity;

import java.time.LocalDate;

import com.shinhan_hackathon.the_family_guardian.domain.family.entity.Family;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "User")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"password", "family"})
@DynamicInsert
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false, length = 50)
	private String username; // userId

	@Column(nullable = false, length = 50)
	private String password;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Temporal(TemporalType.DATE)
	private LocalDate birthDate;

	@Column(unique = true, nullable = false, length = 20)
	private String phone;

	@Column(unique = true, length = 20)
	private String accountNumber;

	@Column(length = 20, nullable = false)
	@ColumnDefault("'SUPPORTER'")
	@Enumerated(EnumType.STRING)
	private Level level;

	@ManyToOne
	@JoinColumn(name = "family_id", nullable = true)
	private Family family;

	@Column(length = 20)
	private String relationship; // 가족 관계

	@Column(length = 20, nullable = false)
	@ColumnDefault("'NONE'")
	@Enumerated(EnumType.STRING)
	private Role role; // 패밀리 내부 권한: 'member' 또는 'guardian'

	private String deviceToken;

	@Builder
	public User(String username, String password, String name, Gender gender, LocalDate birthDate, String phone,
				String accountNumber, Level level, Family family, String relationship, Role role, String deviceToken) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.gender = gender;
		this.birthDate = birthDate;
		this.phone = phone;
		this.accountNumber = accountNumber;
		this.level = level;
		this.family = family;
		this.relationship = relationship;
		this.role = role;
		this.deviceToken = deviceToken;
	}

	public Family updateFamily(Family family) {
		this.family = family;
		return this.family;
	}

	public Role updateRole(Role role) {
		this.role = role;
		return this.role;
	}

	public void updateLevel(Level level) {
		this.level = level;
	}

    public String updateDeviceToken(String deviceToken) {
        return this.deviceToken = deviceToken;
    }

	public String updateRelationship(String relationship) {
		return this.relationship = relationship;
	}
}

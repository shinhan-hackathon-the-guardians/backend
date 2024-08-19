package com.shinhan_hackathon.the_family_guardian.domain.user.entity;

import com.shinhan_hackathon.the_family_guardian.domain.family.entity.Family;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "User")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"password", "ssn", "family"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 10)
    private String gender;

    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Column(unique = true, nullable = false, length = 20)
    private String phone;

    @Column(unique = true, nullable = false, length = 20)
    private String ssn;

    @Column(unique = true, length = 20)
    private String accountNumber;

    @Column(length = 20, nullable = false, columnDefinition = "varchar(20) default 'supporter'")
    private String role;

    @ManyToOne
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;

    @Builder
    public User(String username, String password, String name, String gender, Date birthDate, String phone, String ssn,
                String accountNumber, String role, Family family) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phone = phone;
        this.ssn = ssn;
        this.accountNumber = accountNumber;
        this.role = role;
        this.family = family;
    }
}

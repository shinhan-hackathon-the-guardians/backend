package com.shinhan_hackathon.the_family_guardian.domain.approval.entity;

import com.shinhan_hackathon.the_family_guardian.domain.family.entity.Family;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "Approval")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Approval { // 패밀리 가입 요청

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "varchar(10) default 'waiting'")
    private String accepted; // accepted == true 이면,

    @Builder
    public Approval(Family family, User user, String accepted) {
        this.family = family;
        this.user = user;
        this.accepted = accepted;
    }
}

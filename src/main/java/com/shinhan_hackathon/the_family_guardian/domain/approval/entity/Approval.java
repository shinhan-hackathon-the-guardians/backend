package com.shinhan_hackathon.the_family_guardian.domain.approval.entity;

import com.shinhan_hackathon.the_family_guardian.domain.family.entity.Family;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import jakarta.persistence.*;
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

    @Column(nullable = false, columnDefinition = "varchar(10) default 'PROGRESS'")
    @Enumerated(EnumType.STRING)
    private AcceptStatus accepted;

    private String relationship;

    @Builder
    public Approval(Family family, User user, AcceptStatus accepted, String relationship) {
        this.family = family;
        this.user = user;
        this.accepted = accepted;
        this.relationship = relationship;
    }

    public AcceptStatus updateAccepted(AcceptStatus acceptStatus) {
        return this.accepted = acceptStatus;
    }
    public AcceptStatus updateAccepted(boolean acceptStatus) {
        return this.accepted = acceptStatus ? AcceptStatus.ACCEPT : AcceptStatus.REFUSE;
    }
}

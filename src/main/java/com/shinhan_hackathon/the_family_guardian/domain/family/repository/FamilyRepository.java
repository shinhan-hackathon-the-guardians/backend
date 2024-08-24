package com.shinhan_hackathon.the_family_guardian.domain.family.repository;

import com.shinhan_hackathon.the_family_guardian.domain.family.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyRepository extends JpaRepository<Family, Long> {
}

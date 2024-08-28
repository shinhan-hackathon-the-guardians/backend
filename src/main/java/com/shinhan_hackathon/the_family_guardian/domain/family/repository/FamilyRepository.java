package com.shinhan_hackathon.the_family_guardian.domain.family.repository;

import com.shinhan_hackathon.the_family_guardian.domain.family.entity.Family;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FamilyRepository extends JpaRepository<Family, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select f from Family f where f.id = :id")
    Optional<Family> findByIdForUpdate(Long id);
}

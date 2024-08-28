package com.shinhan_hackathon.the_family_guardian.domain.questionbank.repository;

import com.shinhan_hackathon.the_family_guardian.domain.questionbank.entity.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionBankRepository extends JpaRepository<QuestionBank, Long> {

    @Query(value = "select * from question_bank order by rand() limit 20", nativeQuery = true)
    List<QuestionBank> findQuestionTest();

    @Query(value = "select * from question_bank order by rand()", nativeQuery = true)
    List<QuestionBank> findQuestionPractice();
}

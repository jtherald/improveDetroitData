package jtherald.improveDetroitData.repository;

import jtherald.improveDetroitData.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer> {
    @Modifying
    @Query(value = "CREATE TABLE IF NOT EXISTS question_clean SELECT * FROM question", nativeQuery = true)
    void createTableIssue();

    @Modifying
    @Query(value = "UPDATE question_clean set question = REPLACE(REPLACE(REPLACE(question, '\\n', ' '), '\\t', ' '), '\\r', ' '), answer = REPLACE(REPLACE(REPLACE(answer, '\\n', ' '), '\\t', ' '), '\\r', ' ')",
            nativeQuery = true)
    void insertIssueCleanStrings();

}

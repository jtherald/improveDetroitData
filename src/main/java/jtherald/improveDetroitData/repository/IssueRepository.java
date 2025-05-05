package jtherald.improveDetroitData.repository;

import jtherald.improveDetroitData.entity.IssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<IssueEntity, Integer> {
    @Modifying
    @Query(value = "CREATE TABLE IF NOT EXISTS issue_clean SELECT * FROM issue", nativeQuery = true)
    void createTableIssue();

    @Modifying
    @Query(value = "UPDATE issue_clean set description = REPLACE(REPLACE(REPLACE(description, '\\n', ' '), '\\t', ' '), '\\r', ' ')",
            nativeQuery = true)
    void insertIssueCleanStrings();
}

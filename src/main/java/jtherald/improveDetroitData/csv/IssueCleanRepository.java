package jtherald.improveDetroitData.csv;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueCleanRepository extends JpaRepository<IssueCleanEntity, Integer> {
}

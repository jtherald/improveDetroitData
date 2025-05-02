package jtherald.improveDetroitData.repository;

import jtherald.improveDetroitData.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer> {
}

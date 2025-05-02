package jtherald.improveDetroitData.repository;

import jtherald.improveDetroitData.entity.IssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<IssueEntity, Integer> {
    List<IssueEntity> findByIdLessThan(Integer id);

}

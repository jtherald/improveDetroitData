package jtherald.improveDetroitData.repository;

import jtherald.improveDetroitData.entity.RequestTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestTypeRepository extends JpaRepository<RequestTypeEntity, Integer> {
}

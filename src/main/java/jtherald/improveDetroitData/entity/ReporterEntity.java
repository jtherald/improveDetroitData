package jtherald.improveDetroitData.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "reporter")
public class ReporterEntity {
    @Id
    Integer id;
    String name;
    String role;
}

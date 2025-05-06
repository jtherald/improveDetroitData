package jtherald.improveDetroitData.csv;

import jakarta.persistence.*;
import jtherald.improveDetroitData.entity.QuestionEntity;
import jtherald.improveDetroitData.entity.ReporterEntity;
import jtherald.improveDetroitData.entity.RequestTypeEntity;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "issue_clean")
public class IssueCleanEntity {
    @Id
    Integer id;
    String status;
    String summary;
    String description;
    Double lat;
    Double lng;
    OffsetDateTime created_at;
    OffsetDateTime acknowledged_at;
    OffsetDateTime closed_at;

    @ManyToOne(cascade = CascadeType.ALL)
    RequestTypeEntity request_type;

    @ManyToOne(cascade = CascadeType.ALL)
    ReporterEntity reporter;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "issue_id")
    List<QuestionEntity> questions;

}

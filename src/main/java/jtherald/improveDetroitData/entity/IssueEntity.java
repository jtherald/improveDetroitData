package jtherald.improveDetroitData.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "issue")
public class IssueEntity {
    @Id
    Integer id;
    String status;
    String description;
    String summary;
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

package jtherald.improveDetroitData.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "issue")
public class IssueEntity {
    @Id
    Integer id;
    String status;
    String summary;
    String description;
    Double lat;
    Double lng;
    Date created_at;
    Date acknowledged_at;
    Date closed_at;
    String url;

    @ManyToOne(cascade = CascadeType.ALL)
    RequestTypeEntity request_type;

    @ManyToOne(cascade = CascadeType.ALL)
    ReporterEntity reporter;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "issue_id")
    List<QuestionEntity> questions;

}

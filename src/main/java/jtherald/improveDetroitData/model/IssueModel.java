package jtherald.improveDetroitData.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IssueModel {

    Integer id;
    String status;
    String description;
    String summary;
    Double lat;
    Double lng;
    OffsetDateTime created_at;
    OffsetDateTime acknowledged_at;
    OffsetDateTime closed_at;
    ReporterModel reporter;
    RequestTypeModel request_type;
    List<QuestionModel> questions;
}

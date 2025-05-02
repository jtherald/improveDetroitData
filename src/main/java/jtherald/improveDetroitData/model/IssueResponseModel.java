package jtherald.improveDetroitData.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IssueResponseModel {

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

    ReporterModel reporter;

    RequestTypeResponseModel request_type;

    List<QuestionResponseModel> questions;
}

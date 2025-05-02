package jtherald.improveDetroitData.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueDTO {
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

    Integer request_type_id;
    Integer reporter_id;

}
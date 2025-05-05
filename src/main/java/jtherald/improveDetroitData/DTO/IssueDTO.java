package jtherald.improveDetroitData.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

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
    OffsetDateTime created_at;
    OffsetDateTime acknowledged_at;
    OffsetDateTime closed_at;

    Integer request_type_id;
    Integer reporter_id;

}
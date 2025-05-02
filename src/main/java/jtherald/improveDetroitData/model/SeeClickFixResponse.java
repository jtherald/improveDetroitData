package jtherald.improveDetroitData.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SeeClickFixResponse {
    List<IssueResponseModel> issues;

    MetadataResponseModel metadata;

}

package jtherald.improveDetroitData.mapper;

import jtherald.improveDetroitData.DTO.IssueDTO;
import jtherald.improveDetroitData.DTO.QuestionDTO;
import jtherald.improveDetroitData.entity.IssueEntity;
import jtherald.improveDetroitData.entity.QuestionEntity;
import jtherald.improveDetroitData.model.IssueModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IssueMapper {
    IssueModel toIssueModel(IssueEntity issueEntity);

    List<IssueEntity> toIssueEntity(List<IssueModel> issueResponseModels);

    IssueEntity toIssueEntity(IssueModel issueModel);

    @Mappings({ @Mapping(source = "request_type.id", target = "request_type_id"),
            @Mapping(source = "reporter.id", target = "reporter_id")})
    IssueDTO toIssueDTO(IssueEntity issueEntity);

    @Mappings({ @Mapping(source = "issue_id", target = "issue_id")})
    QuestionDTO toQuestionDTO(QuestionEntity question, Integer issue_id);

}

package jtherald.improveDetroitData.mapper;

import jtherald.improveDetroitData.DTO.IssueDTO;
import jtherald.improveDetroitData.DTO.QuestionDTO;
import jtherald.improveDetroitData.DTO.ReporterDTO;
import jtherald.improveDetroitData.DTO.RequestTypeDTO;
import jtherald.improveDetroitData.entity.IssueEntity;
import jtherald.improveDetroitData.entity.QuestionEntity;
import jtherald.improveDetroitData.entity.ReporterEntity;
import jtherald.improveDetroitData.entity.RequestTypeEntity;
import jtherald.improveDetroitData.model.IssueResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IssueMapper {
    IssueResponseModel toIssueModel(IssueEntity issueEntity);
    IssueEntity toIssueEntity(IssueResponseModel issueModel);

    @Mappings({ @Mapping(source = "request_type.id", target = "request_type_id"),
            @Mapping(source = "reporter.id", target = "reporter_id")})
    IssueDTO toIssueDTO(IssueEntity issueEntity);

    List<ReporterDTO> toReporterDTO(List<ReporterEntity> reporterEntities);

    List<RequestTypeDTO> toRequestTypeDTO(List<RequestTypeEntity> requestTypeEntities);

    @Mappings({ @Mapping(source = "issue_id", target = "issue_id")})
    QuestionDTO toQuestionDTO(QuestionEntity question, Integer issue_id);

}

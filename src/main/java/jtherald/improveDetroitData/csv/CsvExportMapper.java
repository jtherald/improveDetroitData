package jtherald.improveDetroitData.csv;

import jtherald.improveDetroitData.DTO.IssueDTO;
import jtherald.improveDetroitData.DTO.QuestionDTO;
import jtherald.improveDetroitData.DTO.ReporterDTO;
import jtherald.improveDetroitData.DTO.RequestTypeDTO;
import jtherald.improveDetroitData.entity.ReporterEntity;
import jtherald.improveDetroitData.entity.RequestTypeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CsvExportMapper {

    @Mappings({ @Mapping(source = "request_type.id", target = "request_type_id"),
            @Mapping(source = "reporter.id", target = "reporter_id")})
    IssueDTO toIssueDTO(IssueCleanEntity issueEntity);

    @Mappings({ @Mapping(source = "request_type.id", target = "request_type_id"),
            @Mapping(source = "reporter.id", target = "reporter_id")})
    List<IssueDTO> toIssueDTO(List<IssueCleanEntity> issueCleanEntities);

    List<QuestionDTO> toQuestionDTO(List<QuestionCleanEntity> questionCleanEntities);

    List<ReporterDTO> toReporterDTO(List<ReporterEntity> reporterEntities);
    List<RequestTypeDTO> toRequestTypeDTO(List<RequestTypeEntity> requestTypeEntities);
}

package jtherald.improveDetroitData.csv;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jtherald.improveDetroitData.DTO.IssueDTO;
import jtherald.improveDetroitData.DTO.QuestionDTO;
import jtherald.improveDetroitData.DTO.ReporterDTO;
import jtherald.improveDetroitData.DTO.RequestTypeDTO;
import jtherald.improveDetroitData.mapper.IssueMapper;
import jtherald.improveDetroitData.repository.ReporterRepository;
import jtherald.improveDetroitData.repository.RequestTypeRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExportDatabaseToCSVService {
    IssueCleanRepository issueCleanRepository;
    QuestionCleanRepository questionCleanRepository;
    ReporterRepository reporterRepository;
    RequestTypeRepository requestTypeRepository;
    IssueMapper issueMapper;
    CsvExportMapper csvExportMapper;

    public ExportDatabaseToCSVService(IssueCleanRepository issueRepository,
                                      QuestionCleanRepository questionRepository,
                                      ReporterRepository reporterRepository,
                                      RequestTypeRepository requestTypeRepository,
                                      IssueMapper issueMapper,
                                      CsvExportMapper csvExportMapper) {
        this.issueCleanRepository = issueRepository;
        this.questionCleanRepository = questionRepository;
        this.reporterRepository = reporterRepository;
        this.requestTypeRepository = requestTypeRepository;
        this.issueMapper = issueMapper;
        this.csvExportMapper = csvExportMapper;
    }

    public void writeLocalDatabaseToCSV() {

        // this block of code is because the csvExportMapper.toIssueDTO(List<IssueCleanEntity> is not working correctly
        // it is not getting request_type.id and reporter.id, they are set to null.
        // looping this way works for now
        List<IssueCleanEntity> cleanEntities = issueCleanRepository.findAll();
        List<IssueDTO> issueDTOS = new ArrayList<>();
        for(IssueCleanEntity ice : cleanEntities) {
            issueDTOS.add(csvExportMapper.toIssueDTO(ice));
        }

        writeIssueDTOListToFile(issueDTOS,"parsedIssues.csv");
        System.out.println("Issues Saved");

        writeQuestionDTOListToFile(csvExportMapper.toQuestionDTO(questionCleanRepository.findAll()), "parsedQuestions.csv");
        System.out.println("Questions Saved");

        writeReporterDTOListToFile(csvExportMapper.toReporterDTO(reporterRepository.findAll()), "parsedReporters.csv");
        System.out.println("Reporters Saved");

        writeRequestTypeDTOListToFile(csvExportMapper.toRequestTypeDTO(requestTypeRepository.findAll()), "parsedRequestTypes.csv");
        System.out.println("RequestTypes Saved");
    }

    public void writeIssueDTOListToFile(List<IssueDTO> issues, String filename) {
        try {
            CsvMapper mapper = new CsvMapper();
            File outputFile = new File(filename);

            mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.registerModule(new JavaTimeModule());

            CsvSchema schema = CsvSchema.builder().setUseHeader(true)
                    .addColumn("id")
                    .addColumn("status")
                    .addColumn("description")
                    .addColumn("summary")
                    .addColumn("lat")
                    .addColumn("lng")
                    .addColumn("created_at")
                    .addColumn("acknowledged_at")
                    .addColumn("closed_at")
                    .addColumn("request_type_id")
                    .addColumn("reporter_id")
                    .build();

            ObjectWriter writer = mapper.writerFor(IssueDTO.class).with(schema);

            writer.writeValues(outputFile).writeAll(issues);
        } catch (Exception e) {
            System.out.println("Issues: oopsies "+e);
            throw new RuntimeException(e);
        }
    }
    public void writeQuestionDTOListToFile(List<QuestionDTO> questions, String filename) {
        try {
            CsvMapper mapper = new CsvMapper();
            File outputFile = new File(filename);

            mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);

            CsvSchema schema = CsvSchema.builder().setUseHeader(true)
                    .addColumn("id")
                    .addColumn("question")
                    .addColumn("answer")
                    .addColumn("issue_id")
                    .build();

            ObjectWriter writer = mapper.writerFor(QuestionDTO.class).with(schema);
            writer.writeValues(outputFile).writeAll(questions);
        } catch (Exception e) {
            System.out.println("Request Types: oopsies " + e);
            throw new RuntimeException(e);
        }
    }

    public void writeReporterDTOListToFile(List<ReporterDTO> reporters, String filename) {
        try {
            CsvMapper mapper = new CsvMapper();
            File outputFile = new File(filename);

            mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);

            CsvSchema schema = CsvSchema.builder().setUseHeader(true)
                    .addColumn("id")
                    .addColumn("name")
                    .addColumn("role")
                    .build();

            ObjectWriter writer = mapper.writerFor(ReporterDTO.class).with(schema);

            writer.writeValues(outputFile).writeAll(reporters);
        } catch (Exception e) {
            System.out.println("Reporters: oopsies " + e);
            throw new RuntimeException(e);
        }
    }
    public void writeRequestTypeDTOListToFile(List<RequestTypeDTO> requestTypes, String filename) {
        try {
            CsvMapper mapper = new CsvMapper();
            File outputFile = new File(filename);

            mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);

            CsvSchema schema = CsvSchema.builder().setUseHeader(true)
                    .addColumn("id")
                    .addColumn("title")
                    .addColumn("organization")
                    .build();

            ObjectWriter writer = mapper.writerFor(RequestTypeDTO.class).with(schema);

            writer.writeValues(outputFile).writeAll(requestTypes);
        } catch (Exception e) {
            System.out.println("Request Types: oopsies " + e);
            throw new RuntimeException(e);
        }
    }
}

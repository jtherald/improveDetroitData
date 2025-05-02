package jtherald.improveDetroitData;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jakarta.transaction.Transactional;
import jtherald.improveDetroitData.DTO.IssueDTO;
import jtherald.improveDetroitData.DTO.QuestionDTO;
import jtherald.improveDetroitData.DTO.ReporterDTO;
import jtherald.improveDetroitData.DTO.RequestTypeDTO;
import jtherald.improveDetroitData.entity.IssueEntity;
import jtherald.improveDetroitData.entity.QuestionEntity;
import jtherald.improveDetroitData.entity.ReporterEntity;
import jtherald.improveDetroitData.entity.RequestTypeEntity;
import jtherald.improveDetroitData.mapper.IssueMapper;
import jtherald.improveDetroitData.model.IssueResponseModel;
import jtherald.improveDetroitData.model.SeeClickFixResponse;
import jtherald.improveDetroitData.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PullDataService {

    private static final Logger log = LogManager.getLogger(PullDataService.class);

    WebClient webClient;

    IssueRepository issueRepository;
    QuestionRepository questionRepository;
    ReporterRepository reporterRepository;
    RequestTypeRepository requestTypeRepository;

    IssueMapper issueMapper;


    ObjectMapper jsonMapper = new ObjectMapper();

    public PullDataService(WebClient webClient,
                           IssueMapper issueMapper,
                           IssueRepository issueRepository,
                           QuestionRepository questionRepository,
                           ReporterRepository reporterRepository,
                           RequestTypeRepository requestTypeRepository) {
        this.webClient = webClient;
        this.issueMapper = issueMapper;
        this.issueRepository = issueRepository;
        this.questionRepository = questionRepository;
        this.reporterRepository = reporterRepository;
        this.requestTypeRepository = requestTypeRepository;
    }

    public int getAllTrafficIssuesFromInternet() {
        int page = 1;
        int count = 0;
        SeeClickFixResponse response;
        log.info("Starting on Page: "+ page);
        long startTime = System.nanoTime();
        try {
            do {
                int finalPage = page;
                response = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/api/v2/issues/")
                                .queryParam("min_lat", 42.246332)
                                .queryParam("min_lng", -83.2952468)
                                .queryParam("max_lat", 42.443965)
                                .queryParam("max_lng", -82.8771649)
                                .queryParam("per_page", 100)
                                .queryParam("sort_direction", "DESC")
                                .queryParam("sort", "created_at")
                                .queryParam("request_types", 22880)
                                .queryParam("details", "true")
                                .queryParam("page", finalPage)
                                .queryParam("status", "open,acknowledged,closed,archived")
                                .queryParam("fields[issue]", "id,category,summary,description,status,lat,lng,created_at,acknowledged_at,closed_at,request_type,questions,reporter")
                                .build())
                        .retrieve()
                        .bodyToMono(SeeClickFixResponse.class)
                        .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                                .doBeforeRetry(x -> log.info("Retrying page number" + finalPage + "attempt #: "+ x.totalRetries())))
                        .block();
                count += saveResponseIntoDatabase(response);
                log.info("Just Saved Page Number: "+page);
                page++;
            } while (response.getMetadata().getPagination().getNext_page() != null);
            long endTime = System.nanoTime();
            log.info("Issues Imported:"+count+" , Duration (seconds): "+(endTime-startTime)/1000000000);

        } catch (Exception e) {
            System.out.print(e.getMessage());
            throw new RuntimeException(e);
        }
        return count;
    }

    @Transactional
    public int saveResponseIntoDatabase(SeeClickFixResponse response) {
        int saveCount = 0;
        for(IssueResponseModel issue : response.getIssues()) {
            IssueEntity saved = issueRepository.save(issueMapper.toIssueEntity(issue));
            if(saved.getId() != null) {
                saveCount++;
            } else {
                System.out.println("null id");
            }
        }
        return saveCount;
    }

    public void writeLocalDatabaseToCSV() {
        List<IssueEntity> issues = issueRepository.findAll();
        List<IssueDTO> issuesParsed = new ArrayList<>();
        List<QuestionDTO> questions = new ArrayList<>();
        for(IssueEntity issue : issues) {
            //remove all line returns to convert for csv
            String description = issue.getDescription().trim().replace("\n", "").replace("\r", "");
            issue.setDescription(description);
            issuesParsed.add(issueMapper.toIssueDTO(issue));

            for(QuestionEntity q : issue.getQuestions()) {
                //remove all line returns to convert for csv
                String answer = q.getAnswer().trim().replace("\n", "").replace("\r", "");
                q.setAnswer(answer);
                questions.add(issueMapper.toQuestionDTO(q, issue.getId()));
            }
        }
        System.out.println(issuesParsed.size());
        writeIssueDTOListToFile(issuesParsed,"parsedIssues.csv");
        System.out.println("Issues Saved");

        writeQuestionDTOListToFile(questions, "parsedQuestions.csv");
        System.out.println("Questions Saved");

        List<ReporterEntity> reporters = reporterRepository.findAll();
        writeReporterDTOListToFile(issueMapper.toReporterDTO(reporters), "parsedReporters.csv");
        System.out.println("Reporters Saved");

        List<RequestTypeEntity> requestTypes = requestTypeRepository.findAll();
        writeRequestTypeDTOListToFile(issueMapper.toRequestTypeDTO(requestTypes), "parsedRequestTypes.csv");
        System.out.println("RequestTypes Saved");
    }

    public void writeIssueDTOListToFile(List<IssueDTO> issues, String filename) {
        try {
            CsvMapper mapper = new CsvMapper();
            File outputFile = new File(filename);

            mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));

            CsvSchema schema = CsvSchema.builder().setUseHeader(true)
                    .addColumn("id")
                    .addColumn("status")
                    .addColumn("description")
                    .addColumn("lat")
                    .addColumn("lng")
                    .addColumn("created_at")
                    .addColumn("acknowledged_at")
                    .addColumn("closed_at")
                    .addColumn("url")
                    .addColumn("request_type_id")
                    .addColumn("summary")
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
            System.out.println("Request Types: oopsies "+e);
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
            System.out.println("Reporters: oopsies "+e);
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
            System.out.println("Request Types: oopsies "+e);
            throw new RuntimeException(e);
        }
    }
}

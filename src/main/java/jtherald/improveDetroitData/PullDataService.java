package jtherald.improveDetroitData;

import jtherald.improveDetroitData.DTO.IssueDTO;
import jtherald.improveDetroitData.mapper.IssueMapper;
import jtherald.improveDetroitData.model.IssueModel;
import jtherald.improveDetroitData.model.SeeClickFixResponse;
import jtherald.improveDetroitData.repository.IssueRepository;
import jtherald.improveDetroitData.repository.QuestionRepository;
import jtherald.improveDetroitData.repository.ReporterRepository;
import jtherald.improveDetroitData.repository.RequestTypeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.OffsetDateTime;

@Service
public class PullDataService {

    private static final Logger log = LogManager.getLogger(PullDataService.class);

    WebClient webClient;

    IssueRepository issueRepository;
    QuestionRepository questionRepository;
    ReporterRepository reporterRepository;
    RequestTypeRepository requestTypeRepository;

    IssueMapper issueMapper;

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

    /**
     *
     * @param requestTypes - Integer for issue category. blank will get all categories
     * @param afterTimestamp - Will select issues created AFTER Date time YYYY-MM-DDTHH:MM:SSZ, used for updating database. blank will get all issues
     * @return
     */
    public int getIssuesFromInternet(String requestTypes, OffsetDateTime afterTimestamp) {
        int page = 1;
        int count = 0;
        SeeClickFixResponse response;
        log.info("Starting on Page: "+ page);
        long startTime = System.nanoTime();
        try {
            do {
                int currentPage = page;
                response = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/api/v2/issues/")
                                .queryParam("place_url", "detroit")
                                .queryParam("per_page", 100)
                                .queryParam("sort_direction", "ASC")
                                .queryParam("sort", "created_at")
                                .queryParam("request_types", requestTypes)
                                .queryParam("details", "true")
                                .queryParam("page", currentPage)
                                .queryParam("status", "open,acknowledged,closed,archived")
                                .queryParam("fields[issue]", "id,status,summary,description,lat,lng,created_at,acknowledged_at,closed_at,request_type,questions,reporter")
                                .queryParam("after", afterTimestamp)
                                .build())
                        .retrieve()
                        .bodyToMono(SeeClickFixResponse.class)
                        .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                                .doBeforeRetry(x -> log.info("Retrying page number" + currentPage + "attempt #: "+ x.totalRetries())))
                        .block();
                saveResponseIntoDatabase(response);
                count += response.getIssues().size();
                log.info("Just Saved Page Number: "+page);
                page++;
            } while (response.getMetadata().getPagination().getNext_page() != null);
            long endTime = System.nanoTime();
            log.info("Issues Imported:"+count+" , Duration (seconds): "+(endTime-startTime)/1000000000);

            createCleanStringTables();
            log.info("clean tables created");

        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("FAILED ON PAGE NUMBER: "+page);
            throw e;
        }
        return count;
    }

    @Transactional
    public void saveResponseIntoDatabase(SeeClickFixResponse response) {
        issueRepository.saveAll(issueMapper.toIssueEntity(response.getIssues()));
    }

    @Transactional
    public void createCleanStringTables() {
        //create a new 'clean' table for Issue and Question without \r \n \t in the string fields, good for exporting to csv
        issueRepository.createTableIssue();
        issueRepository.insertIssueCleanStrings();

        questionRepository.createTableIssue();
        questionRepository.insertIssueCleanStrings();
    }

    public IssueModel getIssueModelFromLocalDatabase(Integer id) {
        return issueMapper.toIssueModel(issueRepository.findById(id).orElse(null));
    }

    public IssueDTO getIssueDtoFromLocalDatabase(Integer id) {
        return issueMapper.toIssueDTO(issueRepository.findById(id).orElse(null));
    }
}

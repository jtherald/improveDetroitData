package jtherald.improveDetroitData;

import jtherald.improveDetroitData.DTO.IssueDTO;
import jtherald.improveDetroitData.model.IssueModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
public class PullDataController {

    PullDataService pullDataService;

    public PullDataController(PullDataService pullDataService) {
        this.pullDataService = pullDataService;

    }

    /**
     * Call SeeClickFix /issues api for city of Detroit and only category of 'requestTypes' (comma separated list)
     * and save all objects to local database
     * @param requestTypes String comma separated list of category id, see class RequestTypes for short list, blank will return all issues
     * @param afterTimestamp get issues from SeeClickFix that are EQUAL to or newer than this timestamp, used for updating. blank will get all issues
     * @return integer number of issues saved
     */
    @GetMapping("saveTrafficIssuesFromApi")
    public int getTrafficIssuesFromApi(@RequestParam String requestTypes, @RequestParam(required = false) OffsetDateTime afterTimestamp) {
        return pullDataService.getIssuesFromInternet(requestTypes, afterTimestamp);
    }

    /**
     * return a single issue model (full object model) from the local database
     * @param id
     * @return IssueModel
     */
    @GetMapping("getIssueModelFromLocalDatabase")
    public IssueModel getIssueModelFromLocalDatabase(Integer id) {
        return pullDataService.getIssueModelFromLocalDatabase(id);
    }

    /**
     * return a single issue dto (only issue table with id references to reporter and request type) from local database
     * @param id
     * @return IssueDTO
     */
    @GetMapping("getIssueDtoFromLocalDatabase")
    public IssueDTO getIssueDtoFromLocalDatabase(Integer id) {
        return pullDataService.getIssueDtoFromLocalDatabase(id);
    }
}

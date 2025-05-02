package jtherald.improveDetroitData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PullDataController {

    @Autowired
    PullDataService pullDataService;

    @GetMapping("getTrafficIssuesFromApi")
    public int getTrafficIssuesFromApi() {
        return pullDataService.getAllTrafficIssuesFromInternet();
    }


    @GetMapping("writeLocalDatabaseToCSV")
    public void writeLocalDatabaseToCSV() {
        pullDataService.writeLocalDatabaseToCSV();
    }

}

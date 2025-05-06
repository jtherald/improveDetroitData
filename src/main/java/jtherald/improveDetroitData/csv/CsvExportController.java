package jtherald.improveDetroitData.csv;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "csv")
public class CsvExportController {
    ExportDatabaseToCSVService exportDatabaseToCSV;

    public CsvExportController(ExportDatabaseToCSVService exportDatabaseToCSV) {
        this.exportDatabaseToCSV = exportDatabaseToCSV;
    }

    /**
     * export local database tables to csv files
     */
    @GetMapping("exportLocalDatabaseToCSV")
    public void exportLocalDatabaseToCSV() {
        exportDatabaseToCSV.writeLocalDatabaseToCSV();
    }
}

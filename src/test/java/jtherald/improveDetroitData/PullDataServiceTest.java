package jtherald.improveDetroitData;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jtherald.improveDetroitData.model.SeeClickFixResponse;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;

class PullDataServiceTest {

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void praseIssuesFromTextFile() {
        try {
            System.out.println("Hello test starts");
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            String json = testReadIssueResponse("issueResponseTest.txt");
            SeeClickFixResponse response = mapper.readValue(json, SeeClickFixResponse.class);

            System.out.println("Lombok: "+ response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void pars10IssuesFromTextFile() {
        try {
            System.out.println("Hello test starts");
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            String json = testReadIssueResponse("issueResponseTest10.txt");
            SeeClickFixResponse response = mapper.readValue(json, SeeClickFixResponse.class);

            System.out.println("Lombok: "+ response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String testReadIssueResponse(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
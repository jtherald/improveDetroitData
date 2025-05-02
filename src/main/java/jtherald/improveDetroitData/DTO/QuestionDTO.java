package jtherald.improveDetroitData.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {
    Integer id;
    String question;
    String answer;
    Integer issue_id;
}

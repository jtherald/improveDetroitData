package jtherald.improveDetroitData.csv;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "question_clean")
public class QuestionCleanEntity {
    @Id
    Integer id;
    String question;
    String answer;
    Integer issue_id;
}

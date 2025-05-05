package jtherald.improveDetroitData.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionResponseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    String question;
    String answer;
}

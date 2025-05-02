package jtherald.improveDetroitData.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTypeDTO {
    Integer id;
    String title;
    String organization;
}
